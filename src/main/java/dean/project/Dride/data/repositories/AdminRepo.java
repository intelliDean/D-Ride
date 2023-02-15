package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin, Long> {
}
