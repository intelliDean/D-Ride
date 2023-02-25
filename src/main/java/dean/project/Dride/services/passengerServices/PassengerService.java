package dean.project.Dride.services.passengerServices;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.dto.response.UserUpdateResponse;
import dean.project.Dride.data.models.Passenger;
import org.springframework.data.domain.Page;

public interface PassengerService {
    RegisterResponse register(UserRegisterRequest userRegisterRequest);

    Passenger getPassengerById(Long userId);

    Passenger getPassengerByName(String name);

    UserUpdateResponse updatePassengerInfo(Long passId, JsonPatch updatePayload);

    void deletePassenger(Long id);

    void deleteAllPassengers();

    Page<Passenger> getAllPassengers(int pageNumber);

    Passenger savePassenger(Passenger passenger);

}
