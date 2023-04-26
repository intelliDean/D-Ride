package dean.project.Dride.controller;

import dean.project.Dride.data.dto.request.AllRideRequest;
import dean.project.Dride.data.dto.response.entity_dtos.RideDTO;
import dean.project.Dride.services.ride_services.RideService;
import dean.project.Dride.utilities.Paginate;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dean.project.Dride.utilities.RideUrls.*;


@RestController
@AllArgsConstructor
@RequestMapping(RIDE_BASE_URL)
public class RideController {
    private final RideService rideService;

    @GetMapping(ALL_RIDES)
    //@Secured(value ="ADMINISTRATOR")
    public ResponseEntity<Paginate<RideDTO>> getAllRides(@RequestParam int pageNumber) {
        Paginate<RideDTO> rides = rideService.getAllRides(pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping(DRIVER_AND_ID)
    //@Secured(value ={"ADMINISTRATOR", "DRIVER"})
    public ResponseEntity<Paginate<RideDTO>> getAllRidesByDriver(@PathVariable Long driverId, @RequestParam int pageNumber) {
        Paginate<RideDTO> rides = rideService.getAllRidesByDriver(driverId, pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping(PASSENGER_AND_ID)
    //@Secured(value ={"ADMINISTRATOR", "PASSENGER"})
    public ResponseEntity<Paginate<RideDTO>> getAllRidesByPassenger(@PathVariable Long passengerId, @RequestParam int pageNumber) {
        Paginate<RideDTO> rides = rideService.getAllRidesByPassenger(passengerId, pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping(PASSENGER_AND_DRIVER)
    //@Secured(value ={"ADMINISTRATOR", "PASSENGER", "DRIVER"})
    public ResponseEntity<Paginate<RideDTO>> getAllRidesByPassengerAndDriver(@RequestBody AllRideRequest request) {
        Paginate<RideDTO> rides = rideService.getAllRidesByPassengerAndDriver(request);
        return ResponseEntity.ok(rides);
    }
}