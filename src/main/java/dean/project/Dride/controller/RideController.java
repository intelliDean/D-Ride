package dean.project.Dride.controller;

import dean.project.Dride.data.dto.request.AllRideRequest;
import dean.project.Dride.data.dto.response.entity_dtos.RideDTO;
import dean.project.Dride.services.ride_services.RideService;
import dean.project.Dride.utilities.Paginate;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ride")
public class RideController {
    private final RideService rideService;

    @GetMapping("/all")
    //@Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Paginate<RideDTO>> getAllRides(@RequestParam int pageNumber) {
        Paginate<RideDTO> rides = rideService.getAllRides(pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/driver/{driverId}")
    //@Secured(value ={"ADMINISTRATOR", "DRIVER"})
    public ResponseEntity<Paginate<RideDTO>> getAllRidesByDriver(
            @PathVariable Long driverId, @RequestParam int pageNumber) {
        Paginate<RideDTO> rides = rideService.getAllRidesByDriver(driverId, pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/passenger/{passengerId}")
    //@Secured(value ={"ADMINISTRATOR", "PASSENGER"})
    public ResponseEntity<Paginate<RideDTO>> getAllRidesByPassenger(
            @PathVariable Long passengerId, @RequestParam int pageNumber) {
        Paginate<RideDTO> rides = rideService.getAllRidesByPassenger(passengerId, pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/passenger_and_driver")
    //@Secured(value ={"ADMINISTRATOR", "PASSENGER", "DRIVER"})
    public ResponseEntity<Paginate<RideDTO>> getAllRidesByPassengerAndDriver(
            @RequestBody AllRideRequest request) {
        Paginate<RideDTO> rides = rideService.getAllRidesByPassengerAndDriver(request);
        return ResponseEntity.ok(rides);
    }
}