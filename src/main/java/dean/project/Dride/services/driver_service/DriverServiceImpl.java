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
import dean.project.Dride.services.user_service.CurrentUserService;
import dean.project.Dride.services.cloud.CloudService;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.services.ride_services.RideService;
import dean.project.Dride.utilities.DrideUtilities;
import dean.project.Dride.utilities.Paginate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Optional;

import static dean.project.Dride.data.models.Role.DRIVER;
import static dean.project.Dride.exceptions.ExceptionMessage.*;
import static dean.project.Dride.utilities.Constants.*;


@Service
@AllArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {
    private final CurrentUserService currentUserService;
    private final DriverRepository driverRepository;
    private final CloudService cloudService;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final RideService rideService;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;

    @Override
    public GlobalApiResponse register(RegisterRequest request) {
        User user = DrideUtilities.createUser(request.getCreateUser());
        user.getRoles().add(DRIVER);

        var imageUrl = cloudService.upload(request.getLicenseImage());
        if (imageUrl == null) throw new ImageUploadException(DRIVER_REG_FAILED);

        Driver driver = new Driver();
        driver.setUser(user);
        int age = DrideUtilities.calculateAge(request.getDateOfBirth());
        if (age < 18) throw new DrideException(TOO_YOUNG);
        driver.setAge(age);
        driver.setDriverLicense(DriverLicense.builder()
                .licenseImage(imageUrl)     //I will have to call Federal Road Safety Corps (FRSC) API to
                .licenseNumber(request.getLicenseNumber())      //verify the driver license number before registration could be completed
                .build());
        Driver savedDriver = driverRepository.save(driver);

        user = savedDriver.getUser();
        String response = mailService.sendHTMLMail(emailRequest(user.getEmail(), user.getName(), user.getId()));
        return response == null
                ? globalResponse.message(DRIVER_REG_FAILED).build()
                : globalResponse.id(savedDriver.getId()).message(REG_SUCCESS).build();
    }

    @Override
    public GlobalApiResponse completeRegistration(
            Long driverId,
            CompleteDriverRequest driverRequest,
            RefereeRequest refereeRequest) {
        Driver driver = getDriver(driverId);
        Driver updatedDriver = completeDriver(driver, driverRequest);

        Referee referee = createReferee(refereeRequest);

        updatedDriver.setReferee(referee);
        return globalResponse.message("Driver information updated successfully").build();
    }

    private Referee createReferee(RefereeRequest refereeRequest) {
        int age = DrideUtilities.calculateAge(refereeRequest.getDateOfBirth());
        Address address = modelMapper.map(refereeRequest, Address.class);
        Referee referee = modelMapper.map(refereeRequest, Referee.class);
        referee.setAge(age);
        referee.setAddress(address);
        return referee;
    }

    private Driver completeDriver(Driver driver, CompleteDriverRequest driverRequest) {
        Address address = modelMapper.map(driverRequest, Address.class);
        BankInformation bankInformation = modelMapper.map(driverRequest, BankInformation.class);
        driver.setAddress(address);
        driver.setBankInformation(bankInformation);
        driver.setGender(driverRequest.getGender());
        driver.setPhoneNumber(driverRequest.getPhoneNumber());
        return driver;
    }


    private EmailNotificationRequest emailRequest(String email, String name, Long userId) {
        EmailNotificationRequest request = new EmailNotificationRequest();
        request.setSubject(USER_SUBJECT);
        request.getTo().add(new Recipient(name, email));
        String template = DrideUtilities.driverWelcomeMail();
        String content = String.format(template, name, DrideUtilities.generateVerificationLink(userId));
        request.setHtmlContent(content);
        return request;
    }

    private Driver currentDriver() {
        return getDriverByUserId(currentUserService.currentUser().getId())
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Optional<Driver> getDriverBy(Long driverId) {
        return driverRepository.findById(driverId);
    }

    @Override
    public DriverDTO getDriverById(Long driverId) {
        return modelMapper.map(getDriver(driverId), DriverDTO.class);
    }

    private Driver getDriver(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(UserNotFoundException::new);
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
    public DriverDTO updateDriver(JsonPatch jsonPatch) {
        Driver driver = currentDriver();
        ObjectMapper objectMapper = new ObjectMapper();
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
    public GlobalApiResponse ratePassenger(RateRequest rateRequest) {
        Ride ride = rideService.getRideByPassengerIdAndDriverIdRideStatus(
                rateRequest.getRateeId(), currentDriver().getId(), Status.END_RIDE);
        ride.setPassengerRating(rateRequest.getRating());
        rideService.save(ride);
        String passengerName = ride.getPassenger().getUser().getName();
        String rating = rateRequest.getRating().toString();
        return globalResponse
                .message(String.format(RATING, passengerName, rating))
                .build();
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
        Ride ride = rideService.getRideByPassengerIdAndRideStatus(
                request.getPassengerId(),
                Status.BOOKED);
        Driver driver = currentDriver();

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
                request.getPassengerId(), currentDriver().getId(), Status.ACCEPTED);

        ride.setRideStatus(Status.START_RIDE);
        ride.setStartTime(LocalDateTime.now().toString());
        rideService.save(ride);

        return globalResponse
                .message("Ride started successfully")
                .build();
    }

    @Override
    public GlobalApiResponse endRide(EndRideRequest request) {
        Ride ride = rideService.getRideByPassengerIdAndDriverIdRideStatus(
                request.getPassengerId(), currentDriver().getId(), Status.START_RIDE);

        ride.setRideStatus(Status.END_RIDE);
        ride.setEndTime(LocalDateTime.now().toString());
        ride.setPassengerRating(request.getRating());
        //TODO WILL STILL NEED GOOGLE API TO CALCULATE THE ACTUAL FARE INSTEAD OF MOCKING IT

        rideService.save(ride);
        return globalResponse
                .message("Ride ended")
                .fare(ride.getFare())   //todo: I should call google Api to calculate the eventual fare
                .build();
    }
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

