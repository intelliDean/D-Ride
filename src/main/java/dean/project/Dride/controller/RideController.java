package dean.project.Dride.controller;

import dean.project.Dride.data.dto.request.AllRideRequest;
import dean.project.Dride.data.dto.response.entity_dtos.RideDTO;
import dean.project.Dride.services.ride_services.RideService;
import dean.project.Dride.utilities.Paginate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Ride")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ride")
public class RideController {
    private final RideService rideService;

    @GetMapping("/all")
    //@Secured(value ="ADMINISTRATOR")
    @Operation(summary = "fetch all rides")
    public ResponseEntity<Paginate<RideDTO>> getAllRides(
            @Parameter(
                    name = "pageNumber",
                    description = "page to view",
                    required = true
            )
            @RequestParam int pageNumber) {
        Paginate<RideDTO> rides = rideService.getAllRides(pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/driver/{driverId}")
    //@Secured(value ={"ADMINISTRATOR", "DRIVER"})
    @Operation(summary = "fetch rides of a driver")
    public ResponseEntity<Paginate<RideDTO>> getAllRidesByDriver(
            @Parameter(
                    name = "driverId",
                    description = "The ID of driver whose ride to view",
                    required = true
            )
            @PathVariable Long driverId,
            @Parameter(
                    name = "pageNumber",
                    description = "page number to view",
                    required = true
            )
            @RequestParam int pageNumber) {
        Paginate<RideDTO> rides = rideService.getAllRidesByDriver(driverId, pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/passenger/{passengerId}")
    //@Secured(value ={"ADMINISTRATOR", "PASSENGER"})
    @Operation(summary = "fetch rides of a passenger")
    public ResponseEntity<Paginate<RideDTO>> getAllRidesByPassenger(
            @Parameter(
                    name = "passengerId",
                    description = "The ID of passenger whose ride to view",
                    required = true
            )
            @PathVariable Long passengerId,
            @Parameter(
                    name = "pageNumber",
                    description = "page number to view",
                    required = true
            )
            @RequestParam int pageNumber) {
        Paginate<RideDTO> rides = rideService.getAllRidesByPassenger(passengerId, pageNumber);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/passenger_and_driver")
    //@Secured(value ={"ADMINISTRATOR", "PASSENGER", "DRIVER"})
    @Operation(summary = "All rides of a driver and a passenger")
    public ResponseEntity<Paginate<RideDTO>> getAllRidesByPassengerAndDriver(
            @Parameter(
                    name = "request",
                    description = "DTO with passenger and driver details  ",
                    required = true
            )
            @RequestBody AllRideRequest request) {
        Paginate<RideDTO> rides = rideService.getAllRidesByPassengerAndDriver(request);
        return ResponseEntity.ok(rides);
    }
}