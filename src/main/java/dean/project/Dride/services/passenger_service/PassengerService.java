package dean.project.Dride.services.passenger_service;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.data.dto.request.BookRideRequest;
import dean.project.Dride.data.dto.request.RateDriverRequest;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.request.RideRequest;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.dto.response.BookRideResponse;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Passenger;

import java.util.Optional;

public interface PassengerService {
    RegisterResponse register(RegisterPassengerRequest registerRequest);
    Passenger getPassengerById(Long passengerId);

    void savePassenger(Passenger passenger);
    Optional<Passenger> getPassengerBy(Long passengerId);
    Passenger updatePassenger(Long passengerId, JsonPatch updatePayload);

    Paginate<Passenger> getAllPassengers(int pageNumber);

    void deletePassenger(Long id);

    ApiResponse attemptBookRide(BookRideRequest bookRideRequest);
    Passenger getPassengerByEmail(String email);

    BookRideResponse<?> bookRide(RideRequest request);
    ApiResponse rateDriver(RateDriverRequest request);
    Optional<Passenger> getPassengerByUserId(Long userId);

}
