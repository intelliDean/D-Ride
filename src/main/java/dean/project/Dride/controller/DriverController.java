package dean.project.Dride.controller;


import dean.project.Dride.data.dto.request.RegisterDriverRequest;
import dean.project.Dride.data.dto.response.ApiResponse;
import dean.project.Dride.data.dto.response.RegisterResponse;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.services.driverServices.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vi/driver")
public class DriverController {
    private final DriverService driverService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@ModelAttribute RegisterDriverRequest registerDriverRequest) {
        try {
            RegisterResponse response = driverService.register(registerDriverRequest);
            return ResponseEntity.ok(response);
        } catch (DrideException ex) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(ex.getMessage())
                            .build());
        }
    }
}
