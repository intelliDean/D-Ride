package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepo extends JpaRepository<Passenger, Long> {
}
