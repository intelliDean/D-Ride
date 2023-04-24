package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.data.dto.request.BookRideRequest;
import dean.project.Dride.data.dto.request.RateDriverRequest;
import dean.project.Dride.data.dto.request.RegisterPassengerRequest;
import dean.project.Dride.data.dto.request.RideRequest;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.services.user_service.UserService;
import dean.project.Dride.services.passenger_service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passenger")
@AllArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;
    private final UserService userService;



    @PostMapping
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterPassengerRequest registerPassengerRequest){
        RegisterResponse registerResponse = passengerService.register(registerPassengerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }


    @GetMapping("{passengerId}")
    @Secured(value ={"ADMINISTRATOR", "PASSENGER"})
    public ResponseEntity<Passenger> getPassengerById(@PathVariable Long passengerId){
        Passenger foundPassenger = passengerService.getPassengerById(passengerId);
        return ResponseEntity.status(HttpStatus.OK).body(foundPassenger);
    }

    @GetMapping("/all/{pageNumber}")
    @Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Paginate<Passenger>> getAllPassengers(@PathVariable int pageNumber){
        Paginate<Passenger> response =  passengerService.getAllPassengers(pageNumber);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{passengerId}")
    @Secured(value ="PASSENGER")
    public ResponseEntity<?> updatePassenger(@PathVariable Long passengerId, @RequestBody JsonPatch updatePatch){
        try {
            var response = passengerService.updatePassenger(passengerId, updatePatch);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("{passengerId}")
    @Secured(value ="ADMINISTRATOR")
    public ResponseEntity<?> deletePassenger(@PathVariable Long passengerId){
        passengerService.deletePassenger(passengerId);
        return ResponseEntity.ok("Passenger deleted successfully");
    }
    @PostMapping("book")
    @Secured(value ="PASSENGER")
    public ResponseEntity<ApiResponse> attemptBookRide(@RequestBody BookRideRequest request) {
        ApiResponse response = passengerService.attemptBookRide(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/bookRide")
    @Secured(value ="PASSENGER")
    public ResponseEntity<?> bookRide(@RequestBody RideRequest request) {
        var response = passengerService.bookRide(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    @Secured(value ="PASSENGER")
    public ResponseEntity<?> getCurrentPassenger(){
        var response = userService.CurrentAppUser();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/rate")
    @Secured(value ="PASSENGER")
    public ResponseEntity<ApiResponse> rateDriver(@RequestBody RateDriverRequest request) {
        ApiResponse response = passengerService.rateDriver(request);
        return ResponseEntity.ok(response);
    }
}
