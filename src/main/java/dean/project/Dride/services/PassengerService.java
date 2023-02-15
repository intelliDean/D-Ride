package dean.project.Dride.services;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.UpdatePassengerResponse;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Passenger;

public interface PassengerService {
    RegisterResponse register(RegisterPassengerRequest registerPassengerRequest);
    Passenger getPassengerById(Long id);
    UpdatePassengerResponse updatePassenger(Long passId, JsonPatch updatePayload);

}
