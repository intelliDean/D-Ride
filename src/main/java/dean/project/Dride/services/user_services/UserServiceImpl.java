package dean.project.Dride.services.user_services;

import dean.project.Dride.services.cloud_business.CloudService;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.Users;
import dean.project.Dride.data.models.Driver;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.repositories.UserRepository;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.exceptions.UserNotFoundException;
import dean.project.Dride.services.driverServices.DriverService;
import dean.project.Dride.services.passengerServices.PassengerService;
import dean.project.Dride.utilities.DrideUtilities;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final PassengerService passengerService;
    private final DriverService driverService;
    private final CloudService cloudService;
    private final UserRepository userRepository;


    @Override
    public ApiResponse uploadProfileImage(Long userId, MultipartFile profileImage) {
        Optional<Driver> foundDriver = Optional.empty();
        Optional<Passenger> foundPassenger;

        foundPassenger = getOptionalPassenger(userId);
        if (foundPassenger.isEmpty()) {     // TODO: 26-Feb-23 there will be problem is a situation where the id is present in both driver and passenger. I will refactor later
            foundDriver = getOptionalDriver(userId);
        }
        if (foundPassenger.isEmpty() && foundDriver.isEmpty()) {
            throw new UserNotFoundException(String.format("User with ID %d could not be found", userId));
        }

        String imageUrl = cloudService.upload(profileImage);
        foundPassenger.ifPresent(passenger -> savePassengerProfileImage(passenger, imageUrl));
        foundDriver.ifPresent(driver -> saveDriverProfileImage(driver, imageUrl));

        return ApiResponse.builder()
                .message("Upload successful")
                .build();
    }

    private void saveDriverProfileImage(Driver driver, String imageUrl) {
        driver.getUsers().setProfileImage(imageUrl);
        driverService.saveDriver(driver);
    }

    private void savePassengerProfileImage(Passenger passenger, String imageUrl) {
        passenger.getUsers().setProfileImage(imageUrl);
        passengerService.savePassenger(passenger);
    }

    private Optional<Passenger> getOptionalPassenger(Long passengerId) {
        return passengerService.getPassengerOp(passengerId);
    }
    private Optional<Driver> getOptionalDriver(Long driverId) {
        return driverService.getDriverOp(driverId);
    }

    @Override
    public ApiResponse verifyAccount(Long userId, String token) {
        if (DrideUtilities.isValidToken(token)) {
           return verificationResponse(userId);
        }
        throw new DrideException(String.format("Account Verification of User %d Failed", userId));
    }

    @Override
    public Users getByEmail(String email) {
        return userRepository.findDetailsByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User could not be found"));
    }

    private ApiResponse verificationResponse(Long userId) {
        Optional<Passenger> foundPassenger;
        Optional<Driver> foundDriver = Optional.empty();

        foundPassenger = getOptionalPassenger(userId);
        if (foundPassenger.isEmpty()) {
            foundDriver = getOptionalDriver(userId);
        }
        if (foundPassenger.isEmpty() && foundDriver.isEmpty()) {
            throw new UserNotFoundException(String.format("User with ID %d could not be found", userId));
        }
        foundDriver.ifPresent(this :: enableDriverAccount);
        foundPassenger.ifPresent(this :: enablePassengerAccount);

        return ApiResponse.builder()
                .message(String.format("User with ID %d enabled", userId))
                .build();
    }

    private void enablePassengerAccount(Passenger passenger) {
        passenger.getUsers().setIsEnabled(true);
        passengerService.savePassenger(passenger);
    }

    private void enableDriverAccount(Driver driver) {
        driver.getUsers().setIsEnabled(true);
        driverService.saveDriver(driver);
    }
}