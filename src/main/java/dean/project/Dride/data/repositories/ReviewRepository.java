package dean.project.Dride.data.repositories;


import dean.project.Dride.data.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
