package dean.project.Dride.services.passengerServices;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.UpdatePassengerResponse;
import dean.project.Dride.data.dto.request.PassengerDto;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Passenger;

public interface PassengerService {
    RegisterResponse register(RegisterPassengerRequest registerPassengerRequest);
    Passenger getById(Long id);
    UpdatePassengerResponse updateField(Long passId, JsonPatch updatePayload);
    UpdatePassengerResponse updatePassenger(Long id, PassengerDto passengerDto);
    void deletePassenger(Long id);

}
