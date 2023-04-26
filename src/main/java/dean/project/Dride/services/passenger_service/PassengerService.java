package dean.project.Dride.services.passenger_service;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.BookRideRequest;
import dean.project.Dride.data.dto.request.RateDriverRequest;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.request.RideRequest;
import dean.project.Dride.data.dto.response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.BookRideResponse;
import dean.project.Dride.data.dto.response.PassengerDTO;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.utilities.Paginate;

import java.util.Optional;

public interface PassengerService {
    GlobalApiResponse register(RegisterPassengerRequest registerRequest);
    PassengerDTO getPassengerById(Long passengerId);
    void savePassenger(Passenger passenger);
    Optional<Passenger> getPassengerBy(Long passengerId);
    PassengerDTO updatePassenger(Long passengerId, JsonPatch updatePayload);
    Paginate<PassengerDTO> getAllPassengers(int pageNumber);
    void deletePassenger(Long id);
    GlobalApiResponse attemptBookRide(BookRideRequest bookRideRequest);
    PassengerDTO getPassengerByEmail(String email);
    BookRideResponse<?> bookRide(RideRequest request);
    GlobalApiResponse rateDriver(RateDriverRequest request);
    Optional<Passenger> getPassengerByUserId(Long userId);
}