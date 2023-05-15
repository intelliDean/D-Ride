package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository
        extends JpaRepository<CreditCard, Long> {
}
