package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.DrideToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrideTokenRepository extends JpaRepository<DrideToken, Long> {
    Optional<DrideToken> findByAccessToken(String accessToken);
}
