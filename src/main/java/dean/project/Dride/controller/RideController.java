package dean.project.Dride.controller;

import dean.project.Dride.config.app.Paginate;
import dean.project.Dride.data.dto.request.AllRideRequest;
import dean.project.Dride.data.models.Ride;
import dean.project.Dride.services.ride_services.RideService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ride")
public class RideController {
    private final RideService rideService;

    @GetMapping("/all")
    public ResponseEntity<Paginate<Ride>> getAllRides(@RequestParam int pageNumber) {
        Paginate<Ride> rides = rideService.getAllRides(pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<Paginate<Ride>> getAllRidesByDriver(@PathVariable Long driverId, @RequestParam int pageNumber) {
        Paginate<Ride> rides = rideService.getAllRidesByDriver(driverId, pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<Paginate<Ride>> getAllRidesByPassenger(@PathVariable Long passengerId, @RequestParam int pageNumber) {
        Paginate<Ride> rides = rideService.getAllRidesByPassenger(passengerId, pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/passenger_and_driver")
    public ResponseEntity<Paginate<Ride>> getAllRidesByPassengerAndDriver(@RequestBody AllRideRequest request) {
        Paginate<Ride> rides = rideService.getAllRidesByPassengerAndDriver(request);
        return ResponseEntity.ok(rides);
    }
}
