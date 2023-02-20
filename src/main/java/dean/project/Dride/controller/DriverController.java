package dean.project.Dride.controller;


import dean.project.Dride.services.driverServices.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vi/driver")
public class DriverController {
    private final DriverService driverService;

}
