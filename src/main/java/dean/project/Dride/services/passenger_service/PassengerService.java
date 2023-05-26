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
    GlobalApiResponse register(RegisterRequest request);
    PassengerDTO getPassenger();
    void savePassenger(Passenger passenger);
    PassengerDTO updatePassenger(JsonPatch updatePayload);
    Paginate<PassengerDTO> getAllPassengers(int pageNumber);
    void deletePassenger();
    GlobalApiResponse attemptBookRide(BookRideRequest bookRideRequest);
    BookRideResponse<?> bookRide(RideRequest request);
    GlobalApiResponse rateDriver(RateRequest request);
    Optional<Passenger> getPassengerByUserId(Long userId);
}