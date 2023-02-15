package dean.project.Dride.services.driverServices;

import dean.project.Dride.data.repositories.DriverRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;

}
