package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findPassengerByUsers_Name(String userName);

}
