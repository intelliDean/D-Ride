package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.BookRideRequest;
import dean.project.Dride.data.dto.request.RateDriverRequest;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.request.RideRequest;
import dean.project.Dride.data.dto.response.BookRideResponse;
import dean.project.Dride.data.dto.response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.PassengerDTO;
import dean.project.Dride.services.passenger_service.PassengerService;
import dean.project.Dride.services.user_service.UserService;
import dean.project.Dride.utilities.Paginate;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passenger")
@AllArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;
    private final UserService userService;


    @PostMapping
    public ResponseEntity<GlobalApiResponse> register(@RequestBody RegisterPassengerRequest registerPassengerRequest) {
        GlobalApiResponse registerResponse = passengerService.register(registerPassengerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }


    @GetMapping("{passengerId}")
    //@Secured(value ={"ADMINISTRATOR", "PASSENGER"})
    public ResponseEntity<PassengerDTO> getPassengerById(@PathVariable Long passengerId) {
        PassengerDTO foundPassenger = passengerService.getPassengerById(passengerId);
        return ResponseEntity.status(HttpStatus.OK).body(foundPassenger);
    }

    @GetMapping("/all/{pageNumber}")
    //@Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Paginate<PassengerDTO>> getAllPassengers(@PathVariable int pageNumber) {
        Paginate<PassengerDTO> response = passengerService.getAllPassengers(pageNumber);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{passengerId}")
    //@Secured(value ="PASSENGER")
    public ResponseEntity<?> updatePassenger(@PathVariable Long passengerId, @RequestBody JsonPatch updatePatch) {
        try {
            PassengerDTO response = passengerService.updatePassenger(passengerId, updatePatch);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("{passengerId}")
    // @Secured(value ="ADMINISTRATOR")
    public ResponseEntity<String> deletePassenger(@PathVariable Long passengerId) {
        passengerService.deletePassenger(passengerId);
        return ResponseEntity.ok("Passenger deleted successfully");
    }

    @PostMapping("book")
    //@Secured(value ="PASSENGER")
    public ResponseEntity<GlobalApiResponse> attemptBookRide(@RequestBody BookRideRequest request) {
        GlobalApiResponse response = passengerService.attemptBookRide(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bookRide")
    //@Secured(value ="PASSENGER")
    public ResponseEntity<BookRideResponse<?>> bookRide(@RequestBody RideRequest request) {
        var response = passengerService.bookRide(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    // @Secured(value ="PASSENGER")
    public ResponseEntity<?> getCurrentPassenger() {
        var response = userService.CurrentAppUser();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rate")
    //@Secured(value ="PASSENGER")
    public ResponseEntity<GlobalApiResponse> rateDriver(@RequestBody RateDriverRequest request) {
        GlobalApiResponse response = passengerService.rateDriver(request);
        return ResponseEntity.ok(response);
    }
}
