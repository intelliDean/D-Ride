package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findDetailsByEmail(String email);
}
