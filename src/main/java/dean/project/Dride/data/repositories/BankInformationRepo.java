package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.BankInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankInformationRepo extends JpaRepository<BankInformation, Long> {

}
