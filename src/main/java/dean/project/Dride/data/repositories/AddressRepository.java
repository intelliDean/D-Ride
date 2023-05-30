package dean.project.Dride.data.repositories;


import dean.project.Dride.data.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository
        extends JpaRepository<Address, Long> {
}
