package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.RateRequest;
import dean.project.Dride.data.dto.request.RegisterRequest;
import dean.project.Dride.data.dto.request.RideRequest;
import dean.project.Dride.data.dto.response.api_response.BookRideResponse;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.PassengerDTO;
import dean.project.Dride.services.user_service.utility.UtilityUserImpl;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.services.passenger_service.PassengerService;
import dean.project.Dride.utilities.Paginate;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/passenger")
@AllArgsConstructor
public class PassengerController {
    private final UtilityUserImpl utilityUserImpl;
    private final PassengerService passengerService;
    private final MailService userService;


    @PostMapping
    public ResponseEntity<GlobalApiResponse> register(
            @RequestBody RegisterRequest create) {
        GlobalApiResponse registerResponse = passengerService.register(create);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }


    @GetMapping
    //@Secured(value ={"ADMINISTRATOR", "PASSENGER"})
    public ResponseEntity<PassengerDTO> getPassengerById() {
        PassengerDTO foundPassenger = passengerService.getPassenger();
        return ResponseEntity.status(HttpStatus.OK).body(foundPassenger);
    }

    @GetMapping("/all")
    //@Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Paginate<PassengerDTO>> getAllPassengers(@RequestParam int pageNumber) {
        Paginate<PassengerDTO> response = passengerService.getAllPassengers(pageNumber);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("update")
    //@Secured(value ="PASSENGER")
    public ResponseEntity<?> updatePassenger(
            @RequestBody JsonPatch updatePatch) {
        try {
            PassengerDTO response = passengerService
                    .updatePassenger( updatePatch);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("delete")
    // @Secured(value ="ADMINISTRATOR")
    public ResponseEntity<String> deletePassenger() {
        passengerService.deletePassenger();
        return ResponseEntity.ok("Passenger deleted successfully");
    }

    @PostMapping("/bookRide")
    //@Secured(value ="PASSENGER")
    public ResponseEntity<BookRideResponse<?>> bookRide(@RequestBody RideRequest request) {
        var response = passengerService.bookRide(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/rate")
    //@Secured(value ="PASSENGER")
    public ResponseEntity<GlobalApiResponse> rateDriver(@RequestBody RateRequest request) {
        GlobalApiResponse response = passengerService.rateDriver(request);
        return ResponseEntity.ok(response);
    }
}
