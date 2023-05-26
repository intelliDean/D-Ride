package dean.project.Dride.services.driver_service;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.*;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.DriverDTO;
import dean.project.Dride.data.models.Driver;
import dean.project.Dride.utilities.Paginate;

import java.util.Optional;

public interface DriverService {
    GlobalApiResponse register(RegisterRequest request);

    GlobalApiResponse completeRegistration(
            Long driverId,
            CompleteDriverRequest driverRequest,
            RefereeRequest refereeRequest);


    DriverDTO getCurrentDriver();
    void saveDriver(Driver driver);
    GlobalApiResponse acceptRide(AcceptRideRequest request);

    GlobalApiResponse startRide(StartRideRequest request);

    GlobalApiResponse endRide(EndRideRequest request);

    Paginate<DriverDTO> getAllDrivers(int pageNumber);

    DriverDTO updateDriver(JsonPatch jsonPatch);

    Optional<Driver> getDriverByUserId(Long userId);

    GlobalApiResponse ratePassenger(RateRequest rateRequest);
}
