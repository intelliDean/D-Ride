package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.*;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.DriverDTO;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.services.driver_service.DriverService;
import dean.project.Dride.utilities.Paginate;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RestController
@RequestMapping("/api/v1/driver")
@AllArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;

    @PostMapping(value = "/register", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GlobalApiResponse> register(
            @Valid @ModelAttribute RegisterRequest registerRequest) {
        try {
            GlobalApiResponse response = driverService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DrideException exception) {
            return ResponseEntity.badRequest()
                    .body(globalResponse
                            .message(exception.getMessage())
                            .build());
        }
    }
    @PostMapping("complete/{driverId}")
    public ResponseEntity<GlobalApiResponse> completeRegistration(
            @PathVariable Long driverId,
            @RequestBody CompleteDriverRequest driverRequest,
            @RequestBody RefereeRequest refereeRequest) {
        GlobalApiResponse response = driverService.completeRegistration(driverId, driverRequest, refereeRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    //@Secured(value ={"ADMIN", "DRIVER"})
    public ResponseEntity<DriverDTO> currentDriver() {
        DriverDTO driver = driverService.getCurrentDriver();
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/all/{pageNumber}")
    //@Secured(value ="ADMIN")
    public ResponseEntity<Paginate<DriverDTO>> getAllDrivers(@PathVariable int pageNumber) {
        Paginate<DriverDTO> driver = driverService.getAllDrivers(pageNumber);
        return ResponseEntity.ok(driver);
    }

    @PatchMapping("/update")
    //@Secured(value ={"ADMIN", "DRIVER"})
    public ResponseEntity<DriverDTO> updateDriver(@RequestBody JsonPatch jsonPatch) {
        DriverDTO driver = driverService.updateDriver(jsonPatch);
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
    @PostMapping("/rate")
    public ResponseEntity<GlobalApiResponse> ratePassenger(@RequestBody RateRequest request) {
        GlobalApiResponse response = driverService.ratePassenger(request);
        return ResponseEntity.ok(response);
    }
}
