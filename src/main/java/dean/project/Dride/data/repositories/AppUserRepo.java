package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.Details;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<Details, Long> {
}
