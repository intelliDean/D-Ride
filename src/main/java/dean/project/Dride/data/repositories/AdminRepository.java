package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository
        extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminByUser_Id(Long userId);

    Optional<Admin> findByUserEmail(String userEmail);
}
