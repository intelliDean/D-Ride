package dean.project.Dride.services.passenger_service;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.*;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.api_response.BookRideResponse;
import dean.project.Dride.data.dto.response.entity_dtos.PassengerDTO;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.utilities.Paginate;

import java.util.Optional;

public interface PassengerService {
    GlobalApiResponse register(RegisterPassengerRequest request);
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