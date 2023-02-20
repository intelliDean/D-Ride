package dean.project.Dride.services.driverServices;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.response.UserUpdateResponse;
import dean.project.Dride.data.dto.entitydtos.DriverDto;
import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DriverService {
    RegisterResponse register(UserRegisterRequest userRegisterRequest, MultipartFile licenseImage);
//    DriverDto  getById(Long id);
//    List<DriverDto> getAllDriver();
//    UserUpdateResponse patchUpdate(Long driverId, JsonPatch updatePayload);
//    UserUpdateResponse updateDriver(Long id, DriverDto passengerDto);
//    void deleteById(Long id);
//    void deleteAll();
}
