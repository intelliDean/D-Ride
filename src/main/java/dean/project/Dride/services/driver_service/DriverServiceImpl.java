package dean.project.Dride.services.driver_service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import dean.project.Dride.data.dto.request.*;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.DriverDTO;
import dean.project.Dride.data.models.*;
import dean.project.Dride.data.repositories.DriverRepository;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.exceptions.ImageUploadException;
import dean.project.Dride.exceptions.UserNotFoundException;
import dean.project.Dride.services.cloud.CloudService;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.services.ride_services.RideService;
import dean.project.Dride.utilities.DrideUtilities;
import dean.project.Dride.utilities.Paginate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;

import static dean.project.Dride.exceptions.ExceptionMessage.*;
import static dean.project.Dride.utilities.Constants.*;


@Service
@AllArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final CloudService cloudService;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final RideService rideService;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;

    @Override
    public GlobalApiResponse register(RegisterDriverRequest request) {
        User user = createUser(request);
        var imageUrl = cloudService.upload(request.getLicenseImage());
        if (imageUrl == null) throw new ImageUploadException(DRIVER_REG_FAILED);

        Driver driver = new Driver();
        driver.setUser(user);
        int age = getAge(request);
        driver.setAge(age);
        driver.setLicenseImage(imageUrl);
        Driver savedDriver = driverRepository.save(driver);

        user = savedDriver.getUser();
        EmailNotificationRequest emailRequest = buildNotificationRequest(
                user.getEmail(), user.getName(), user.getId());
        String response = mailService.sendHTMLMail(emailRequest);
        return response == null
                ? globalResponse.message(DRIVER_REG_FAILED).build()
                : globalResponse.id(savedDriver.getId()).message(REG_SUCCESS).build();
    }

    private static int getAge(RegisterDriverRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate dateOfBirth = LocalDate.parse(request.getDateOfBirth(), formatter);
        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(dateOfBirth, currentDate);
        int age = period.getYears();
        if (age < 18) throw new DrideException(TOO_YOUNG);
        return age;
    }

    @NotNull
    private User createUser(RegisterDriverRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now().toString());
        user.setRoles(new HashSet<>());
        user.getRoles().add(Role.DRIVER);
        return user;
    }

    private EmailNotificationRequest buildNotificationRequest(String email, String name, Long userId) {
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.setSubject(USER_SUBJECT);
        request.getTo().add(new Recipient(name, email));
        String template = DrideUtilities.driverWelcomeMail();
        String content = String.format(template, name, DrideUtilities.generateVerificationLink(userId));
        request.setHtmlContent(content);
        return request;
    }

    @Override
    public Optional<Driver> getDriverBy(Long driverId) {
        return driverRepository.findById(driverId);
    }

    @Override
    public DriverDTO getDriverById(Long driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(UserNotFoundException::new);
        return modelMapper.map(driver, DriverDTO.class);
    }

    @Override
    public Paginate<DriverDTO> getAllDrivers(int pageNumber) {
        int page = pageNumber < 1 ? 0 : pageNumber - 1;
        Pageable pageable = PageRequest.of(page, NUMBER_OF_ITEMS_PER_PAGE);
        Page<Driver> pagedDriver = driverRepository.findAll(pageable);
        Type returnedDrivers = new TypeToken<Paginate<DriverDTO>>() {
        }.getType();
        return modelMapper.map(pagedDriver, returnedDrivers);
    }

    @Override
    public DriverDTO updateDriver(Long driverId, JsonPatch jsonPatch) {
        ObjectMapper objectMapper = new ObjectMapper();
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(UserNotFoundException::new);

        JsonNode jsonNode = objectMapper.convertValue(driver, JsonNode.class);
        try {
            JsonNode updatedNode = jsonPatch.apply(jsonNode);

            Driver updatedDriver = objectMapper.convertValue(updatedNode, Driver.class);
            driverRepository.save(updatedDriver);
            return modelMapper.map(driver, DriverDTO.class);

        } catch (JsonPatchException e) {
            throw new DrideException(UPDATE_FAILED);
        }
    }

    @Override
    public Optional<Driver> getDriverByUserId(Long userId) {
        return driverRepository.findDriverByUser_Id(userId);
    }

    @Override
    public void saveDriver(Driver driver) {
        driverRepository.save(driver);
    }

    @Override
    public DriverDTO getDriverByEMail(String email) {
        Driver driver = driverRepository.findDriverByUser_Email(email)
                .orElseThrow(UserNotFoundException::new);
        return modelMapper.map(driver, DriverDTO.class);
    }

    @Override
    public GlobalApiResponse acceptRide(AcceptRideRequest request) {
        Ride ride = rideService
                .getRideByPassengerIdAndRideStatus(
                        request.getPassengerId(),
                        Status.BOOKED
                );

        Driver driver = getDriverBy(request.getDriverId())
                .orElseThrow(UserNotFoundException::new);

        ride.setDriver(driver);
        ride.setRideStatus(Status.ACCEPTED);
        rideService.save(ride);

        return globalResponse
                .id(ride.getId())
                .phoneNumber(driver.getPhoneNumber())
                .profileImage(driver.getUser().getProfileImage())
                .name(driver.getUser().getName())
                .build();
    }

    @Override
    public GlobalApiResponse startRide(StartRideRequest request) {
        Ride ride = rideService.getRideByPassengerIdAndDriverIdRideStatus(
                request.getPassengerId(), request.getDriverId(), Status.ACCEPTED);

        ride.setRideStatus(Status.START_RIDE);
        ride.setStartTime(LocalDateTime.now().toString());
        rideService.save(ride);

        return globalResponse
                .message(RIDE_STARTED)
                .build();
    }

    @Override
    public GlobalApiResponse endRide(EndRideRequest request) {
        Ride ride = rideService.getRideByPassengerIdAndDriverIdRideStatus(
                request.getPassengerId(), request.getDriverId(), Status.START_RIDE);

        ride.setRideStatus(Status.END_RIDE);
        ride.setEndTime(LocalDateTime.now().toString());
        ride.setPassengerRating(request.getRating());
        //TODO WILL STILL NEED GOOGLE API TO CALCULATE THE ACTUAL FARE INSTEAD OF MOCKING IT

        rideService.save(ride);
        return globalResponse
                .message(RIDE_ENDED)
                .fare(ride.getFare())   //todo: I should call google Api to calculate the eventual fare
                .build();
    }

//    public static char[] reverse(char[] arrays) {
//        for (int i = 0, j = arrays.length - 1; i < arrays.length/2; i++, j--) {
//            char temp = arrays[j];
//            arrays[j] = arrays[i];
//            arrays[i] = temp;
//        }
//        return arrays;
//    }

//  public static int[] merge(int[] array1, int[] array2) {
//    int[] mergedArrays = new int[array1.length + array2.length];
//    int i = 0, j = 0, k = 0;
//
//    while (i < array1.length && j < array2.length) {
//        if (array1[i] < array2[j]) {
//            mergedArrays[k++] = array1[i++];
//        } else {
//            mergedArrays[k++] = array2[j++];
//        }
//    }
//
//    while (i < array1.length) {
//        mergedArrays[k++] = array1[i++];
//    }
//
//    while (j < array2.length) {
//        mergedArrays[k++] = array2[j++];
//    }
//
//    return mergedArrays;
//}
//     public static void main(String[] args) {
////        char[] arrays = {'s', 'e', 'm', 'i', 'c', 'o', 'l', 'o', 'n'};
////        char[] result = reverse(arrays);
////        System.out.println(result);
//         int[] array1 = {1, 3, 5, 7};
//         int[] array2 = {2, 4, 6, 8};
//
//         System.out.println(Arrays.toString(merge(array1, array2)));
//
//     }
}
