package dean.project.Dride.services.driver_service;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.config.app.Paginate;
import dean.project.Dride.data.dto.request.AcceptRideRequest;
import dean.project.Dride.data.dto.request.EndRideRequest;
import dean.project.Dride.data.dto.request.RegisterDriverRequest;
import dean.project.Dride.data.dto.request.StartRideRequest;
import dean.project.Dride.data.dto.response.AcceptRideResponse;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Driver;

import java.util.Optional;

public interface DriverService {
    RegisterResponse register(RegisterDriverRequest request);
    Optional<Driver> getDriverBy(Long driverId);
    Driver getDriverById(Long driverId);
    void saveDriver(Driver driver);
    Driver getDriverByEMail(String email);
    AcceptRideResponse acceptRide(AcceptRideRequest request);
    ApiResponse startRide(StartRideRequest request);
    ApiResponse endRide(EndRideRequest request);
    Paginate<Driver> getAllDrivers(int pageNumber);
    Driver updateDriver(Long driverId, JsonPatch jsonPatch);
    Optional<Driver> getDriverByUserId(Long userId);
}
