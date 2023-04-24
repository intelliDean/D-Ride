package dean.project.Dride.services.user_service;

import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.config.security.users.AuthenticatedUser;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.Admin;
import dean.project.Dride.data.models.Driver;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.models.User;
import dean.project.Dride.data.repositories.UserRepository;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.exceptions.UserNotFoundException;
import dean.project.Dride.services.admin_service.AdminService;
import dean.project.Dride.services.cloud.CloudService;
import dean.project.Dride.services.driver_service.DriverService;
import dean.project.Dride.services.passenger_service.PassengerService;
import dean.project.Dride.utilities.DrideUtilities;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.Optional;

import static dean.project.Dride.utilities.DrideUtilities.NUMBER_OF_ITEMS_PER_PAGE;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final PassengerService passengerService;
    private final DriverService driverService;
    private final CloudService cloudService;
    private final AdminService adminService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public ApiResponse uploadProfileImage(MultipartFile profileImage, Long appUserId) {
        User user = getByUserId(appUserId);

        String imageUrl = cloudService.upload(profileImage);
        if (imageUrl.isEmpty()) {
            throw new DrideException("Image upload failed");
        }

        user.setProfileImage(imageUrl);
        userRepository.save(user);
        return ApiResponse.builder()
                .message("SUCCESS")
                .build();
    }
    private UserRecord getUserRecord(Long appUserId) {
        if (!userRepository.existsById(appUserId)) throw new DrideException("User does not exist");

        Optional<Driver> driver = Optional.empty();
        Optional<Admin> admin = Optional.empty();
        Optional<Passenger> passenger = passengerService.getPassengerByUserId(appUserId);
        if (passenger.isEmpty()) {
            driver = driverService.getDriverByUserId(appUserId);
            if (driver.isEmpty()) {
                admin = adminService.getAdminByUserId(appUserId);
                if (admin.isEmpty()) {
                    throw new DrideException("User could not be found");
                }
            }
        }
        return new UserRecord(driver, admin, passenger);
    }

    private record UserRecord(      //TODO: I created a record of UserRecord instead of a concrete class
            Optional<Driver> driver,
            Optional<Admin> admin,
            Optional<Passenger> passenger) {
    }

    @Override
    public ApiResponse verifyAccount(Long userId, String token) {
        //TODO: To get verified, the jwt in the link sent will be checked if signed and not expired
        //TODO: the userId in the link sent will be checked if existed in db
        //TODO: if these conditions are met, then the account will be verified and enabled
        if (DrideUtilities.isTokenSigned(token)) {
            return getVerifiedResponse(userId);
        }
        throw new DrideException(
                String.format("account verification for user with %d failed", userId)
        );
    }

    private ApiResponse getVerifiedResponse(Long userId) {
        UserRecord userRecord = getUserRecord(userId);
        userRecord.driver.ifPresent(this::enableDriverAccount);
        userRecord.passenger.ifPresent(this::enablePassengerAccount);
        userRecord.admin.ifPresent(this::enableAdminAccount);
        return ApiResponse.builder()
                .message("SUCCESS")
                .build();
    }

    private void enablePassengerAccount(Passenger passenger) {
        passenger.getUser().setIsEnabled(true);
        passengerService.savePassenger(passenger);
    }

    private void enableDriverAccount(Driver driver) {
        driver.getUser().setIsEnabled(true);
        driverService.saveDriver(driver);
    }
    private void enableAdminAccount(Admin admin) {
        admin.getUser().setIsEnabled(true);
        adminService.saveAdmin(admin);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("user with email not found"));
    }

    @Override
    public User getByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("user could not be found"));
    }

    @Override
    public Paginate<User> getAllUsers(int pageNumber) {
        if (pageNumber < 0) pageNumber = 0;
        else pageNumber -= 1;

        Pageable pageable = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE);
        Page<User> users = userRepository.findAll(pageable);
        Type paginatedUsers = new TypeToken<Paginate<User>>() {
        }.getType();
        return modelMapper.map(users, paginatedUsers);
    }

    @Override
    public String CurrentAppUser() {
        try {
            AuthenticatedUser user  = (AuthenticatedUser) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();

            return user+" nothing is here";
        } catch (Exception e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }
}
