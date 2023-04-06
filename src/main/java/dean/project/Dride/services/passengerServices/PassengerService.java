package dean.project.Dride.services.passengerServices;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.BookRideRequest;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.dto.response.UserUpdateResponse;
import dean.project.Dride.data.models.Passenger;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PassengerService {
    RegisterResponse register(RegisterPassengerRequest registerPassengerRequest);

    Passenger getPassengerById(Long passengerId);
    Optional<Passenger> getPassengerOp(Long passengerId);

    Passenger getPassengerByName(String name);

    UserUpdateResponse updatePassengerInfo(Long passId, JsonPatch updatePayload);

    void deletePassenger(Long id);

    void deleteAllPassengers();

    Page<Passenger> getAllPassengers(int pageNumber);

    void savePassenger(Passenger passenger);
    //ApiResponse bookRide(BookRideRequest bookRideRequest);



}
