package dean.project.Dride.data.repositories;


import dean.project.Dride.data.models.Referee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefereeRepository
        extends JpaRepository<Referee, Long> {
}
