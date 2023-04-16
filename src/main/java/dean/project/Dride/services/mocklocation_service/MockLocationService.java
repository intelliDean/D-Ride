package dean.project.Dride.services.mocklocation_service;

import dean.project.Dride.data.dto.request.Location;
import dean.project.Dride.data.dto.response.GoogleDistanceResponse;

public interface MockLocationService {
    GoogleDistanceResponse getDistanceInformation(Location origin, Location destination);
}
