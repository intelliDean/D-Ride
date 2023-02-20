package dean.project.Dride.services.passengerServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import dean.project.Dride.data.dto.response.UserUpdateResponse;
import dean.project.Dride.data.dto.entitydtos.PassengerDto;
import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.repositories.AppUserRepo;
import dean.project.Dride.data.repositories.PassengerRepository;
import dean.project.Dride.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private static final int NUMBER_OF_ITEMS_PER_PAGE = 3;
    private final ModelMapper modelMapper;


    @Override
    public RegisterResponse register(UserRegisterRequest userRegisterRequest) {
        Details mappedDetails = modelMapper.map(userRegisterRequest, Details.class);
        mappedDetails.setRegisteredAt(LocalDateTime.now().toString());

        Passenger passenger = Passenger.builder().details(mappedDetails).build();

        Passenger savedPassenger = passengerRepository.save(passenger);

        return getRegisterResponse(savedPassenger);
    }

    @Override
    public Passenger getById(Long userId) {
        return passengerRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException(String
                        .format("Passenger with ID %d not found", userId)));
    }

    @Override
    public Passenger getByName(String name) {
        Passenger passenger = passengerRepository.findPassengerByDetailsName(name);
        if (passenger == null) {
            throw new UserNotFoundException("Passenger not found");
        }
        return passenger;
    }


    @Override
    public UserUpdateResponse updateField(Long passId, JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Passenger passenger = getById(passId);

        // convert passenger object to node
        JsonNode node = mapper.convertValue(passenger, JsonNode.class);
        try {
            // apply patch
            JsonNode updatedNode = updatePayload.apply(node);
            // convert node back to passenger object
            var updatedPassenger = mapper.convertValue(updatedNode, Passenger.class);
            var savedPassenger = passengerRepository.save(updatedPassenger);

            return getUpdateResponse(savedPassenger);
        } catch (JsonPatchException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public UserUpdateResponse updatePassenger(Long id, PassengerDto passengerDto) {
        Passenger passenger = getById(id);
        passenger.setDetails(passengerDto.getDetails());
        passenger.setGender(passengerDto.getGender());
        passenger.setPhoneNumber(passengerDto.getPhoneNumber());

        Passenger returnedPassenger = passengerRepository.save(passenger);

        return getUpdateResponse(returnedPassenger);
    }

    @Override
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        passengerRepository.deleteAll();
    }

    @Override
    public List<Passenger> getAll() {
        return passengerRepository.findAll();
    }

    private static RegisterResponse getRegisterResponse(Passenger passenger) {
        return RegisterResponse.builder()
                .id(passenger.getId())
                .code(HttpStatus.CREATED.value())
                .message("User Registration Successful")
                .isSuccessful(true)
                .build();
    }

    private static UserUpdateResponse getUpdateResponse(Passenger passenger) {
        UserUpdateResponse patch = new UserUpdateResponse();
        patch.setMessage(String.format("Passenger with ID %d updated successfully", passenger.getId()));

        return patch;
    }

}
