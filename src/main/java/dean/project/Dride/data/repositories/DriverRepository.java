package dean.project.Dride.data.repositories;


import dean.project.Dride.data.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository
        extends JpaRepository<Driver, Long> {

    Optional<Driver> findDriverByUser_Email(String email);
    Optional<Driver> findDriverByUser_Id(Long userId);
}
