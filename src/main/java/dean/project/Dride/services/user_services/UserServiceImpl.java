package dean.project.Dride.services.user_services;

import dean.project.Dride.cloud_business.CloudService;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.Driver;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.exceptions.UserNotFoundException;
import dean.project.Dride.services.driverServices.DriverService;
import dean.project.Dride.services.passengerServices.PassengerService;
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

    @Override
    public ApiResponse uploadProfileImage(Long userId, MultipartFile profileImage) {
        Optional<Driver> foundDriver = Optional.empty();
        Optional<Passenger> foundPassenger;

        foundPassenger = getOptionalPassenger(userId);
        if (foundPassenger.isEmpty()) {     // TODO: 26-Feb-23 there will be problem is a situation where the id is present in both driver and passenger
            foundDriver = getOptionalDriver(userId);
        }
        if (foundPassenger.isEmpty() && foundDriver.isEmpty()) {
            throw new UserNotFoundException(String.format("User with ID %d could not be found", userId));
        }

        String imageUrl = cloudService.upload(profileImage);
        foundPassenger.ifPresent(passenger -> passengerProfileImage(passenger, imageUrl));
        foundDriver.ifPresent(driver -> driverProfileImage(driver, imageUrl));

        return n;
    }

    private void driverProfileImage(Driver driver, String imageUrl) {
        driver.getDetails().setProfileImage(imageUrl);
        driverService.saveDriver(driver);
    }

    private void passengerProfileImage(Passenger passenger, String imageUrl) {
        passenger.getDetails().setProfileImage(imageUrl);
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
        return null;
    }
}
