package dean.project.Dride.services.ride_services;


import dean.project.Dride.data.dto.response.entity_dtos.RideDTO;
import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.data.dto.request.AllRideRequest;
import dean.project.Dride.data.models.Ride;
import dean.project.Dride.data.models.Status;

public interface RideService {
    Ride save(Ride ride);
    Ride getRideByPassengerIdAndRideStatus(Long passengerId, Status status);
    Ride getRideByPassengerIdAndDriverIdRideStatus(Long passengerId, Long driverId, Status status);
    Paginate<RideDTO> getAllRides(int pageNumber);
    Paginate<RideDTO> getAllRidesByDriver(Long driverId, int pageNumber);
    Paginate<RideDTO> getAllRidesByPassenger(Long passengerId, int pageNumber);
    Paginate<RideDTO> getAllRidesByPassengerAndDriver(AllRideRequest request);
}
