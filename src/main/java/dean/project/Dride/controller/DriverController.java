package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.data.dto.request.AcceptRideRequest;
import dean.project.Dride.data.dto.request.EndRideRequest;
import dean.project.Dride.data.dto.request.RegisterDriverRequest;
import dean.project.Dride.data.dto.request.StartRideRequest;
import dean.project.Dride.data.dto.response.AcceptRideResponse;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.models.Driver;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.services.driver_service.DriverService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/driver")
@AllArgsConstructor
public class DriverController {

    private final DriverService driverService;


    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@Valid @ModelAttribute RegisterDriverRequest registerDriverRequest) {
        try {
            var response = driverService.register(registerDriverRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DrideException exception) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder()
                            .message(exception.getMessage())
                            .build());
        }
    }

    @GetMapping("{driverId}")
    @Secured(value ={"ADMIN", "DRIVER"})
    public ResponseEntity<Driver> getDriver(@PathVariable Long driverId) {
        Driver driver = driverService.getDriverById(driverId);
        return ResponseEntity.ok(driver);
    }
    @GetMapping("/all/{pageNumber}")
    @Secured(value ="ADMIN")
    public ResponseEntity<Paginate<Driver>> getAllDrivers(@PathVariable int pageNumber) {
        Paginate<Driver> driver = driverService.getAllDrivers(pageNumber);
        return ResponseEntity.ok(driver);
    }
    @PatchMapping("/update/{driverId}")
    @Secured(value ={"ADMIN", "DRIVER"})
    public ResponseEntity<Driver> updateDriver(@PathVariable Long driverId, @RequestBody JsonPatch jsonPatch) {
        Driver driver = driverService.updateDriver(driverId, jsonPatch);
        return ResponseEntity.ok(driver);
    }
    @PostMapping("/accept")
    @Secured(value = "DRIVER")
    public ResponseEntity<AcceptRideResponse> acceptRide(@RequestBody AcceptRideRequest request) {
        AcceptRideResponse response = driverService.acceptRide(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    @Secured(value = "DRIVER")
    public ResponseEntity<ApiResponse> startRide(@RequestBody StartRideRequest request) {
        ApiResponse response = driverService.startRide(request);
        return ResponseEntity.ok(response);
    } @PostMapping("/end")
    @Secured(value = "DRIVER")
    public ResponseEntity<ApiResponse> endRide(@RequestBody EndRideRequest request) {
        ApiResponse response = driverService.endRide( request);
        return ResponseEntity.ok(response);
    }
}
