package dean.project.Dride.controller;

import com.github.fge.jsonpatch.JsonPatch;
import dean.project.Dride.data.dto.request.*;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.data.dto.response.entity_dtos.DriverDTO;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.services.driver_service.DriverService;
import dean.project.Dride.utilities.Paginate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Tag(name = "Driver")
@RestController
@RequestMapping("/api/v1/driver")
@AllArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final GlobalApiResponse.GlobalApiResponseBuilder globalResponse;

    @PostMapping(value = "/register", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Register driver")
    public ResponseEntity<GlobalApiResponse> register(
            @Parameter(
                    name = "registerRequest",
                    description = "DTO with driver registration info",
                    required = true
            )
            @Valid @ModelAttribute RegisterRequest registerRequest
    ) {
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
    @Operation(summary = "complete driver registration")
    public ResponseEntity<GlobalApiResponse> completeRegistration(
            @Parameter(
                    name = "driverId",
                    description = "Driver ID",
                    required = true
            )
            @PathVariable Long driverId,
            @Parameter(
                    name = "driverRequest",
                    description = "DTO with driver info",
                    required = true
            )
            @RequestBody CompleteDriverRequest driverRequest,
            @Parameter(
                    name = "refereeRequest",
                    description = "DTO with referee info",
                    required = true
            )
            @RequestBody RefereeRequest refereeRequest) {
        GlobalApiResponse response = driverService.completeRegistration(
                driverId, driverRequest, refereeRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    //@Secured(value ={"ADMIN", "DRIVER"})
    @Operation(summary = "current user")
    public ResponseEntity<DriverDTO> currentDriver() {
        DriverDTO driver = driverService.getCurrentDriver();
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/all/{pageNumber}")
    //@Secured(value ="ADMIN")
    @Operation(summary = "fetch all drivers in batches")
    public ResponseEntity<Paginate<DriverDTO>> getAllDrivers(
            @Parameter(
                    name = "pageNumber",
                    description = "The page to view",
                    required = true
            )
            @PathVariable int pageNumber) {
        Paginate<DriverDTO> driver = driverService.getAllDrivers(pageNumber);
        return ResponseEntity.ok(driver);
    }

    @PatchMapping("/update")
    //@Secured(value ={"ADMIN", "DRIVER"})
    @Operation(summary = "update driver by patch")
    public ResponseEntity<DriverDTO> updateDriver(
            @Parameter(
                    name = "jsonPatch",
                    description = "JsonPatch Syntax",
                    required = true
            )
            @RequestBody JsonPatch jsonPatch) {
        DriverDTO driver = driverService.updateDriver(jsonPatch);
        return ResponseEntity.ok(driver);
    }

    @PostMapping("/accept")
    //@Secured(value = "DRIVER")
    @Operation(summary = "to accept ride request")
    public ResponseEntity<GlobalApiResponse> acceptRide(
            @Parameter(
                    name = "request",
                    description = "DTO with accept ride request",
                    required = true
            )
            @RequestBody AcceptRideRequest request) {
        GlobalApiResponse response = driverService.acceptRide(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/start")
    //@Secured(value = "DRIVER")
    @Operation(summary = "To start ride")
    public ResponseEntity<GlobalApiResponse> startRide(
            @Parameter(
                    name = "request",
                    description = "DTO with start ride request",
                    required = true
            )
            @RequestBody StartRideRequest request) {
        GlobalApiResponse response = driverService.startRide(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/end")
    //@Secured(value = "DRIVER")
    @Operation(summary = "to end ride")
    public ResponseEntity<GlobalApiResponse> endRide(
            @Parameter(
                    name = "request",
                    description = "DTO with end ride request",
                    required = true
            )
            @RequestBody EndRideRequest request) {
        GlobalApiResponse response = driverService.endRide(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rate")
    @Operation(summary = "for driver to rate passenger")
    public ResponseEntity<GlobalApiResponse> ratePassenger(
            @Parameter(
                    name = "request",
                    description = "DTO with rate passenger request",
                    required = true
            )
            @RequestBody RateRequest request) {
        GlobalApiResponse response = driverService.ratePassenger(request);
        return ResponseEntity.ok(response);
    }
}
