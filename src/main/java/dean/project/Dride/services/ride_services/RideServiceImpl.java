package dean.project.Dride.services.ride_services;

import dean.project.Dride.data.dto.request.AllRideRequest;
import dean.project.Dride.data.dto.response.entity_dtos.RideDTO;
import dean.project.Dride.data.models.Ride;
import dean.project.Dride.data.models.Status;
import dean.project.Dride.data.repositories.RideRepository;
import dean.project.Dride.exceptions.DrideException;
import dean.project.Dride.utilities.Paginate;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

import static dean.project.Dride.utilities.Constants.NUMBER_OF_ITEMS_PER_PAGE;
import static dean.project.Dride.utilities.Constants.RIDE_NOT_FOUND;


@AllArgsConstructor
@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final ModelMapper modelMapper;

    @Override
    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }

    @Override
    public Ride getRideByPassengerIdAndRideStatus(Long passengerId, Status status) {
        return rideRepository.findRideByPassenger_IdAndRideStatus(passengerId, status)
                .orElseThrow(() -> new DrideException(RIDE_NOT_FOUND));
    }

    @Override
    public Ride getRideByPassengerIdAndDriverIdRideStatus(Long passengerId, Long driverId, Status status) {
        return rideRepository.findByPassenger_IdAndDriver_IdAndRideStatus(passengerId, driverId, status)
                .orElseThrow(() -> new DrideException(RIDE_NOT_FOUND));
    }

    @Override
    public Paginate<RideDTO> getAllRides(int pageNumber) {
        int page = pageNumber < 1 ? 0 : pageNumber - 1;
        Pageable pageable = PageRequest.of(page, NUMBER_OF_ITEMS_PER_PAGE);
        Page<Ride> rides = rideRepository.findAll(pageable);
        Type paginatedRide = new TypeToken<Paginate<RideDTO>>() {
        }.getType();
        return modelMapper.map(rides, paginatedRide);
    }

    @Override
    public Paginate<RideDTO> getAllRidesByDriver(Long driverId, int pageNumber) {
        int page = pageNumber < 1 ? 0 : pageNumber - 1;
        List<Ride> driverRides = rideRepository.findAllByDriver_Id(driverId);
        return getPaginatedRide(page, driverRides);
    }

    @Override
    public Paginate<RideDTO> getAllRidesByPassenger(Long passengerId, int pageNumber) {
        int page = pageNumber < 1 ? 0 : pageNumber - 1;
        List<Ride> passengerRides = rideRepository.findAllByPassenger_Id(passengerId);
        return getPaginatedRide(page, passengerRides);
    }

    @Override
    public Paginate<RideDTO> getAllRidesByPassengerAndDriver(AllRideRequest request) {
        int page = request.getPageNumber()< 1 ? 0 : request.getPageNumber() - 1;
        List<Ride> bothRides = rideRepository.findAllByPassenger_IdAndDriver_Id(
                request.getPassengerId(), request.getDriverId());
        return getPaginatedRide(page, bothRides);
    }

    private Paginate<RideDTO> getPaginatedRide(int pageNumber, List<Ride> ridesList) {
        Pageable pageable = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE);
        Page<Ride> rides = new PageImpl<>(ridesList, pageable, ridesList.size());
        Type paginatedRide = new TypeToken<Paginate<RideDTO>>() {
        }.getType();
        return modelMapper.map(rides, paginatedRide);
    }
}
