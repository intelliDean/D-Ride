package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.entitydtos.PassengerDto;
import dean.project.Dride.data.dto.request.UserRegisterRequest;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.services.passengerServices.PassengerService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/passenger")
public class PassengerController {
    private final PassengerService passengerService;
    private final ModelMapper modelMapper;


    @PostMapping("register")
    public ResponseEntity<?> registerPassenger(@RequestBody UserRegisterRequest userRegisterRequest) {
        RegisterResponse registerResponse = passengerService.register(userRegisterRequest);
        return ResponseEntity.status(registerResponse.getCode()).body(registerResponse);
    }

    @GetMapping("{passengerId}")
    public ResponseEntity<?> getPassengerById(@PathVariable Long passengerId) {
        Passenger passenger = passengerService.getById(passengerId);

        PassengerDto passengerDto = modelMapper.map(passenger, PassengerDto.class);

        return ResponseEntity.status(HttpStatus.OK).body(passengerDto);
    }
    @GetMapping("{name}")
    public ResponseEntity<?> getPassengerByName(@PathVariable String name) {
        Passenger passenger = passengerService.getByName(name);

        PassengerDto passengerDto = modelMapper.map(passenger, PassengerDto.class);

        return ResponseEntity.status(HttpStatus.OK).body(passengerDto);
    }

    @PatchMapping(value = "{passengerId}", consumes = "application/json-patch+json")
    public ResponseEntity<?> updatePassengerField(@PathVariable Long passengerId, @RequestBody JsonPatch updatePatch) {
        try {
            var response = passengerService.updateField(passengerId, updatePatch);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("{passengerId}")
    public ResponseEntity<?> deletePassenger(@PathVariable Long passengerId) {
        passengerService.deletePassenger(passengerId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(String.format("Passenger with ID %d deleted successfully", passengerId));
    }

    @DeleteMapping("deleteAll")
    public ResponseEntity<?> deleteAllPassengers() {
        passengerService.deleteAll();

        return ResponseEntity.status(HttpStatus.OK).body("All Passengers deleted successfully");
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<?>> getAllPassengers() {
        return ResponseEntity.status(HttpStatus.OK).body(passengerService.getAll());
    }


}