package dean.project.Dride.services.ride_services;


import dean.project.Dride.utilities.Paginate;
import dean.project.Dride.data.dto.request.AllRideRequest;
import dean.project.Dride.data.models.Ride;
import dean.project.Dride.data.models.Status;

public interface RideService {
    Ride save(Ride ride);
    Ride getRideByPassengerIdAndRideStatus(Long passengerId, Status status);
    Ride getRideByPassengerIdAndDriverIdRideStatus(Long passengerId, Long driverId, Status status);
    Paginate<Ride> getAllRides(int pageNumber);
    Paginate<Ride> getAllRidesByDriver(Long driverId, int pageNumber);
    Paginate<Ride> getAllRidesByPassenger(Long passengerId, int pageNumber);
    Paginate<Ride> getAllRidesByPassengerAndDriver(AllRideRequest request);
}
