package dean.project.Dride.services.passengerServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import dean.project.Dride.dride_mappers.DrideMappers;
import dean.project.Dride.exceptions.PassengerNotFoundException;
import dean.project.Dride.data.dto.UpdatePassengerResponse;
import dean.project.Dride.data.dto.request.PassengerDto;
import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.repositories.PassengerRepository;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class PassengerServiceImpl implements PassengerService {

    // I use "@AllArgsConstructor and final" instead of @Autowire
    private final PassengerRepository passengerRepository;


    @Override
    public RegisterResponse register(RegisterPassengerRequest registerPassengerRequest) {
        Details details = DrideMappers.mapToDetails(registerPassengerRequest);

        Passenger passenger = new Passenger();
        passenger.setDetails(details);  //I am only interested in the basic details to register

        Passenger savedPassenger = passengerRepository.save(passenger);

        return getRegisterResponse(savedPassenger);
    }

    @Override
    public Passenger getById(Long id) {
      return passengerRepository.findById(id)
              .orElseThrow(()-> new PassengerNotFoundException(
                              String.format("Passenger with ID %d not found", id)
                      )
              );
    }

    @Override
    public UpdatePassengerResponse updateField(Long passId, JsonPatch updatePayload) {
        ObjectMapper mapper = new ObjectMapper();
        Passenger passenger = getById(passId);

        // convert passenger object to node
        JsonNode node = mapper.convertValue(passenger, JsonNode.class);
        try {
            // apply patch
            JsonNode updatedNode = updatePayload.apply(node);
            // convert node back to passenger object
            var updatedPassenger = mapper.convertValue(updatedNode, Passenger.class);

            return getUpdateResponse(updatedPassenger);
        } catch (JsonPatchException ex) {
            log.error(ex.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public UpdatePassengerResponse updatePassenger(Long id, PassengerDto passengerDto) {
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

    private static RegisterResponse getRegisterResponse(Passenger passenger) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(passenger.getId());
        registerResponse.setCode(HttpStatus.CREATED.value());
        registerResponse.setSuccessful(true);
        registerResponse.setMessage("User Registration Successful");

        return registerResponse;
    }
    private static UpdatePassengerResponse getUpdateResponse(Passenger passenger) {
        UpdatePassengerResponse patch = new UpdatePassengerResponse();
        patch.setMessage(String.format("Passenger with ID %d updated successfully", passenger.getId()));

        return patch;
    }

}
