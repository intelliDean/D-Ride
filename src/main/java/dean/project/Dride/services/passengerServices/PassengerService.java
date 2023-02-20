package dean.project.Dride.services.passengerServices;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.entitydtos.PassengerDto;
import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.dto.response.UserUpdateResponse;
import dean.project.Dride.data.models.Passenger;

import java.util.List;

public interface PassengerService {
    RegisterResponse register(UserRegisterRequest userRegisterRequest);

    Passenger getById(Long userId);

    Passenger getByName(String name);

    UserUpdateResponse updateField(Long passId, JsonPatch updatePayload);

    UserUpdateResponse updatePassenger(Long id, PassengerDto passengerDto);

    void deletePassenger(Long id);

    void deleteAll();

    List<Passenger> getAll();

}
