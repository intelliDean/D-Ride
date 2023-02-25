package dean.project.Dride.services.passengerServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.dto.response.UserUpdateResponse;
import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.repositories.PassengerRepository;
import dean.project.Dride.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static dean.project.Dride.utilities.DrideUtilities.NUMBER_OF_ITEMS_PER_PAGE;

@Slf4j
@AllArgsConstructor
@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    //private final ModelMapper modelMapper;


    @Override
    public RegisterResponse register(UserRegisterRequest userRegisterRequest) {
        Details details = mapDetails(userRegisterRequest);

        Passenger passenger = Passenger.builder()
                .details(details)
                .build();

        Passenger savedPassenger = passengerRepository.save(passenger);

        return getRegisterResponse(savedPassenger);
    }

    private static Details mapDetails(UserRegisterRequest userRegisterRequest) {
        return Details.builder()
                .name(userRegisterRequest.getName())
                .email(userRegisterRequest.getEmail())
                .password(userRegisterRequest.getPassword())
                .registeredAt(LocalDateTime.now().toString())
                .build();
    }

    private static RegisterResponse getRegisterResponse(Passenger passenger) {
        return RegisterResponse.builder()
                .id(passenger.getId())
                .code(HttpStatus.CREATED.value())
                .message("User Registration Successful")
                .isSuccessful(true)
                .build();
    }

    @Override
    public Passenger getPassengerById(Long passengerId) {
        return passengerRepository.findById(passengerId)
                .orElseThrow(()->new UserNotFoundException(String
                        .format("Passenger with ID %d not found", passengerId)));
    }

    @Override
    public Passenger getPassengerByName(String passengerName) {
        return passengerRepository.findPassengerByDetailsName(passengerName)
                .orElseThrow(()->new UserNotFoundException("Passenger could not be found"));
    }


    @Override
    public UserUpdateResponse updatePassengerInfo(Long passId, JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Passenger passenger = getPassengerById(passId);

        // convert passenger object to node
        JsonNode node = mapper.convertValue(passenger, JsonNode.class);
        try {
            // apply patch
            JsonNode updatedNode = updatePayload.apply(node);
            // convert node back to passenger object
            var updatedPassenger = mapper.convertValue(updatedNode, Passenger.class);
            var savedPassenger = passengerRepository.save(updatedPassenger);


            return UserUpdateResponse.builder()
                    .message(String.format("Passenger with ID %d updated successfully", passenger.getId()))
                    .build();
        } catch (JsonPatchException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }

    @Override
    public void deleteAllPassengers() {
        passengerRepository.deleteAll();
    }

    @Override
    public Page<Passenger> getAllPassengers(int pageNumber) {
        if (pageNumber < 1) {
            pageNumber = 0;
        } else {
            pageNumber -= 1;
        }
        Pageable pageable = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE);
        return passengerRepository.findAll(pageable);
    }

    @Override
    public Passenger savePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }
}
