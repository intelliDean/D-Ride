package dean.project.Dride.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import dean.project.Dride.MyMapper.MyMapper;
import dean.project.Dride.data.dto.UpdatePassengerResponse;
import dean.project.Dride.data.models.Details;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.repositories.PassengerRepo;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
@Slf4j
@AllArgsConstructor
@Service
public class PassengerServiceImpl implements PassengerService{
    private final PassengerRepo passengerRepo;      //"@AllArgsConstructor and final" instead of @Autowire

    private ModelMapper modelMapper;

    @Override
    public RegisterResponse register(RegisterPassengerRequest registerPassengerRequest) {
        Details details = MyMapper.mapDetails(registerPassengerRequest);
        details.setLocalDateTime(LocalDateTime.now());

        //Details details = fillAppUser(registerPassengerRequest);

        return getRegisterResponse(details);
    }

    @Override
    public Passenger getPassengerById(Long id) {
      return passengerRepo.findById(id).orElseThrow(RuntimeException :: new);
    }

    @Override
    public UpdatePassengerResponse updatePassenger(Long passId, JsonPatch updatePayload) {
        Passenger passenger = getPassengerById(passId);
        JsonNode node =  modelMapper.map(passenger, JsonNode.class);
        try {
          JsonNode node1=  updatePayload.apply(node);
          var res = JsonPatch.fromJson(node1);
          var pass = modelMapper.map(res, Passenger.class);
        } catch(IOException | JsonPatchException ex) {
            log.error(ex.getMessage());
            throw new Exception()
        }



        return null;
    }

    private RegisterResponse getRegisterResponse(Details details) {
        Passenger passenger = new Passenger();
        passenger.setDetails(details);
        Passenger savedPassenger = passengerRepo.save(passenger);
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(savedPassenger.getId());
        registerResponse.setCode(HttpStatus.CREATED.value());
        registerResponse.setSuccessful(true);
        registerResponse.setMessage("User Registration Successful");

        return registerResponse;
    }

    private static Details fillAppUser(RegisterPassengerRequest registerPassengerRequest) { //will use mappers instead
        Details details = new Details();
        details.setName(registerPassengerRequest.getName());
        details.setEmail(registerPassengerRequest.getEmail());
        details.setPassword(registerPassengerRequest.getPassword());
        return details;
    }
}
