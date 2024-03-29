package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.RateRequest;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.request.RideRequest;
import dean.project.Dride.data.dto.response.api_response.BookRideResponse;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.PassengerDTO;
import dean.project.Dride.services.notification.MailService;
import dean.project.Dride.services.passenger_service.PassengerService;
import dean.project.Dride.services.user_service.utility.UtilityUserImpl;
import dean.project.Dride.utilities.Paginate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "register passenger")
    public ResponseEntity<GlobalApiResponse> register(
            @Parameter(
                    name = "create",
                    description = "DTO with passenger info",
                    required = true
            )
            @RequestBody RegisterPassengerRequest create) {
        GlobalApiResponse registerResponse = passengerService.register(create);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }


    @GetMapping
    //@Secured(value ={"ADMINISTRATOR", "PASSENGER"})
    @Operation(summary = "current passenger")
    public ResponseEntity<PassengerDTO> currentPassenger() {
        PassengerDTO foundPassenger = passengerService.getCurrentPassenger();
        return ResponseEntity.status(HttpStatus.OK).body(foundPassenger);
    }

    @GetMapping("/all")
    //@Secured(value ="ADMINISTRATOR")
    @Operation(summary = "fetch all passengers")
    public ResponseEntity<Paginate<PassengerDTO>> getAllPassengers(
            @Parameter(
                    name = "pageNumber",
                    description = "page to view",
                    required = true
            )
            @RequestParam int pageNumber) {
        Paginate<PassengerDTO> response = passengerService.getAllPassengers(pageNumber);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("update")
    //@Secured(value ="PASSENGER")
    @Operation(summary = "update passenger info in patches")
    public ResponseEntity<?> updatePassenger(
            @Parameter(
                    name = "updatePatch",
                    description = "JsonPatch syntax",
                    required = true
            )
            @RequestBody JsonPatch updatePatch) {
        try {
            PassengerDTO response = passengerService
                    .updatePassenger(updatePatch);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("delete")
    // @Secured(value ="ADMINISTRATOR")
    @Operation(summary = "delete passenger")
    public ResponseEntity<String> deletePassenger() {
        passengerService.deletePassenger();
        return ResponseEntity.ok("Passenger deleted successfully");
    }

    @PostMapping("/bookRide")
    //@Secured(value ="PASSENGER")
    @Operation(summary = "passenger books a ride")
    public ResponseEntity<BookRideResponse<?>> bookRide(
            @Parameter(
                    name = "request",
                    description = "Book ride request",
                    required = true
            )
            @RequestBody RideRequest request) {
        var response = passengerService.bookRide(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/rate")
    //@Secured(value ="PASSENGER")
    @Operation(summary = "passenger rates driver")
    public ResponseEntity<GlobalApiResponse> rateDriver(
            @Parameter(
                    name = "request",
                    description = "Rate driver request",
                    required = true
            )
            @RequestBody RateRequest request) {
        GlobalApiResponse response = passengerService.rateDriver(request);
        return ResponseEntity.ok(response);
    }
}
