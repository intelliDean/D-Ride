package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.AcceptRideRequest;
import dean.project.Dride.data.dto.request.EndRideRequest;
import dean.project.Dride.data.dto.request.RegisterDriverRequest;
import dean.project.Dride.data.dto.request.StartRideRequest;
import dean.project.Dride.data.dto.response.DriverDTO;
import dean.project.Dride.data.dto.response.GlobalApiResponse;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.services.driver_service.DriverService;
import dean.project.Dride.utilities.Paginate;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/driver")
@AllArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;


    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalApiResponse> register(@Valid @ModelAttribute RegisterDriverRequest registerDriverRequest) {
        try {
            GlobalApiResponse response = driverService.register(registerDriverRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DrideException exception) {
            return ResponseEntity.badRequest()
                    .body(globalResponse
                            .message(exception.getMessage())
                            .build());
        }
    }

    @GetMapping("{driverId}")
    //@Secured(value ={"ADMIN", "DRIVER"})
    public ResponseEntity<DriverDTO> getDriver(@PathVariable Long driverId) {
        DriverDTO driver = driverService.getDriverById(driverId);
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/all/{pageNumber}")
    //@Secured(value ="ADMIN")
    public ResponseEntity<Paginate<DriverDTO>> getAllDrivers(@PathVariable int pageNumber) {
        Paginate<DriverDTO> driver = driverService.getAllDrivers(pageNumber);
        return ResponseEntity.ok(driver);
    }

    @PatchMapping("/update/{driverId}")
    //@Secured(value ={"ADMIN", "DRIVER"})
    public ResponseEntity<DriverDTO> updateDriver(@PathVariable Long driverId, @RequestBody JsonPatch jsonPatch) {
        DriverDTO driver = driverService.updateDriver(driverId, jsonPatch);
        return ResponseEntity.ok(driver);
    }

    @PostMapping("/accept")
    //@Secured(value = "DRIVER")
    public ResponseEntity<GlobalApiResponse> acceptRide(@RequestBody AcceptRideRequest request) {
        GlobalApiResponse response = driverService.acceptRide(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    //@Secured(value = "DRIVER")
    public ResponseEntity<GlobalApiResponse> startRide(@RequestBody StartRideRequest request) {
        GlobalApiResponse response = driverService.startRide(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/end")
    //@Secured(value = "DRIVER")
    public ResponseEntity<GlobalApiResponse> endRide(@RequestBody EndRideRequest request) {
        GlobalApiResponse response = driverService.endRide(request);
        return ResponseEntity.ok(response);
    }
}
