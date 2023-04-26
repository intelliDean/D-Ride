package dean.project.Dride.services.ride_services;

import dean.project.Dride.data.dto.response.RideDTO;
import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.data.dto.request.AllRideRequest;
import dean.project.Dride.data.models.Ride;
import dean.project.Dride.data.models.Status;
import dean.project.Dride.data.repositories.RideRepository;
import dean.project.Dride.exceptions.DrideException;
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

import static dean.project.Dride.utilities.DrideUtilities.NUMBER_OF_ITEMS_PER_PAGE;


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
                .orElseThrow(() -> new DrideException("Ride not found"));

    }

    @Override
    public Ride getRideByPassengerIdAndDriverIdRideStatus(Long passengerId, Long driverId, Status status) {
        return rideRepository.findByPassenger_IdAndDriver_IdAndRideStatus(passengerId, driverId, status)
                .orElseThrow(() -> new DrideException("Ride not found"));
    }

    @Override
    public Paginate<RideDTO> getAllRides(int pageNumber) {
        if (pageNumber < 0) pageNumber = 0;
        else pageNumber -= 1;

        Pageable pageable = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE);
        Page<Ride> rides = rideRepository.findAll(pageable);
        Type paginatedRide = new TypeToken<Paginate<RideDTO>>() {
        }.getType();
        return modelMapper.map(rides, paginatedRide);
    }

    @Override
    public Paginate<RideDTO> getAllRidesByDriver(Long driverId, int pageNumber) {
        if (pageNumber < 0) pageNumber = 0;
        else pageNumber -= 1;

        List<Ride> driverRides = rideRepository.findAllByDriver_Id(driverId);
        return getPaginatedRide(pageNumber, driverRides);
    }

    @Override
    public Paginate<RideDTO> getAllRidesByPassenger(Long passengerId, int pageNumber) {
        if (pageNumber < 0) pageNumber = 0;
        else pageNumber -= 1;

        List<Ride> passengerRides = rideRepository.findAllByPassenger_Id(passengerId);
        return getPaginatedRide(pageNumber, passengerRides);
    }

    @Override
    public Paginate<RideDTO> getAllRidesByPassengerAndDriver(AllRideRequest request) {
        if (request.getPageNumber() < 0) request.setPageNumber(0);
        else request.setPageNumber(request.getPageNumber() - 1);

        List<Ride> bothRides = rideRepository.findAllByPassenger_IdAndDriver_Id(
                request.getPassengerId(), request.getDriverId());

        return getPaginatedRide(request.getPageNumber(), bothRides);
    }

    private Paginate<RideDTO> getPaginatedRide(int pageNumber, List<Ride> ridesList) {
        Pageable pageable = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE);
        Page<Ride> rides = new PageImpl<>(ridesList, pageable, ridesList.size());
        Type paginatedRide = new TypeToken<Paginate<RideDTO>>() {
        }.getType();
        return modelMapper.map(rides, paginatedRide);
    }
}
