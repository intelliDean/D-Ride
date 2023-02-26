package dean.project.Dride.services.driverServices;

import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Driver;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface DriverService {
    RegisterResponse register(UserRegisterRequest userRegisterRequest, MultipartFile licenseImage);
    Driver getDriverById(Long driverId);
    Driver saveDriver(Driver driver);
    Optional<Driver> getDriverOp(Long driverId);
//    List<DriverDto> getAllDriver();
//    UserUpdateResponse patchUpdate(Long driverId, JsonPatch updatePayload);
//    UserUpdateResponse updateDriver(Long id, DriverDto passengerDto);
//    void deleteById(Long id);
//    void deleteAll();
}
