package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.CreateUser;
import dean.project.Dride.data.dto.request.RateDriverRequest;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.request.RideRequest;
import dean.project.Dride.data.dto.response.api_response.BookRideResponse;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.PassengerDTO;
import dean.project.Dride.services.passenger_service.PassengerService;
import dean.project.Dride.services.user_service.UserService;
import dean.project.Dride.utilities.Paginate;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dean.project.Dride.utilities.PassengerUrls.*;


@RestController
@RequestMapping(PASSENGER_BASE_URL)
@AllArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;
    private final UserService userService;


    @PostMapping
    public ResponseEntity<GlobalApiResponse> register(
            @RequestBody RegisterPassengerRequest create) {
        GlobalApiResponse registerResponse = passengerService.register(create);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }


    @GetMapping(PASSENGER_ID)
    //@Secured(value ={"ADMINISTRATOR", "PASSENGER"})
    public ResponseEntity<PassengerDTO> getPassengerById(@PathVariable Long passengerId) {
        PassengerDTO foundPassenger = passengerService.getPassengerById(passengerId);
        return ResponseEntity.status(HttpStatus.OK).body(foundPassenger);
    }

    @GetMapping(ALL_PASSENGERS)
    //@Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Paginate<PassengerDTO>> getAllPassengers(@PathVariable int pageNumber) {
        Paginate<PassengerDTO> response = passengerService.getAllPassengers(pageNumber);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(PASSENGER_ID)
    //@Secured(value ="PASSENGER")
    public ResponseEntity<?> updatePassenger(
            @PathVariable Long passengerId,
            @RequestBody JsonPatch updatePatch) {
        try {
            PassengerDTO response = passengerService
                    .updatePassenger(passengerId, updatePatch);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping(PASSENGER_ID)
    // @Secured(value ="ADMINISTRATOR")
    public ResponseEntity<String> deletePassenger(@PathVariable Long passengerId) {
        passengerService.deletePassenger(passengerId);
        return ResponseEntity.ok(PASSENGER_DELETED);
    }

    @PostMapping(BOOK_RIDE)
    //@Secured(value ="PASSENGER")
    public ResponseEntity<BookRideResponse<?>> bookRide(@RequestBody RideRequest request) {
        var response = passengerService.bookRide(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(CURRENT_USER)
    // @Secured(value ="PASSENGER")
    public ResponseEntity<?> getCurrentPassenger() {
        var response = userService.CurrentAppUser();
        return ResponseEntity.ok(response);
    }

    @PostMapping(RATE_DRIVER)
    //@Secured(value ="PASSENGER")
    public ResponseEntity<GlobalApiResponse> rateDriver(@RequestBody RateDriverRequest request) {
        GlobalApiResponse response = passengerService.rateDriver(request);
        return ResponseEntity.ok(response);
    }
}
