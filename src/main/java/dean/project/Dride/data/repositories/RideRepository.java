package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.Ride;
import dean.project.Dride.data.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideRepository
        extends JpaRepository<Ride, Long> {
    Optional<Ride> findRideByPassenger_IdAndRideStatus(Long passengerId, Status status);
    Optional<Ride> findByPassenger_IdAndDriver_IdAndRideStatus(Long passengerId, Long driverId, Status status);
    List<Ride> findAllByDriver_Id(Long driverId);
    List<Ride> findAllByPassenger_Id(Long passengerId);
    List<Ride> findAllByPassenger_IdAndDriver_Id(Long passengerId, Long driverId);
}
