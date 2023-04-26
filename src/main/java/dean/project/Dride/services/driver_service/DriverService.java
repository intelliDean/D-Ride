package dean.project.Dride.services.driver_service;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.AcceptRideRequest;
import dean.project.Dride.data.dto.request.EndRideRequest;
import dean.project.Dride.data.dto.request.RegisterDriverRequest;
import dean.project.Dride.data.dto.request.StartRideRequest;
import dean.project.Dride.data.dto.response.entity_dtos.DriverDTO;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.models.Driver;
import dean.project.Dride.utilities.Paginate;

import java.util.Optional;

public interface DriverService {
    GlobalApiResponse register(RegisterDriverRequest request);
    Optional<Driver> getDriverBy(Long driverId);
    DriverDTO getDriverById(Long driverId);
    void saveDriver(Driver driver);
    DriverDTO getDriverByEMail(String email);
    GlobalApiResponse acceptRide(AcceptRideRequest request);
    GlobalApiResponse startRide(StartRideRequest request);
    GlobalApiResponse endRide(EndRideRequest request);
    Paginate<DriverDTO> getAllDrivers(int pageNumber);
    DriverDTO updateDriver(Long driverId, JsonPatch jsonPatch);
    Optional<Driver> getDriverByUserId(Long userId);
}
