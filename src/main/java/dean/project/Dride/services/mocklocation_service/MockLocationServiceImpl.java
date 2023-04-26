package dean.project.Dride.services.mocklocation_service;

//import com.google.maps.model.DistanceMatrixRow;  // TODO: I will change to this when I am able to call google API

import dean.project.Dride.data.dto.request.Location;
import dean.project.Dride.data.dto.response.google_dtos.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static dean.project.Dride.utilities.Constants.*;


@Service
@AllArgsConstructor
public class MockLocationServiceImpl implements MockLocationService {
    @Override
    public GoogleDistanceResponse getDistanceInformation(Location origin, Location destination) {
        return new GoogleDistanceResponse(
                List.of(destination.toString()),
                List.of(origin.toString()),
                List.of(new DistanceMatrixRow(
                                List.of(new DistanceMatrixElement(DistanceMatrixElementStatus.OK,
                                                new GoogleDistance(KM, VALUE1),
                                                new GoogleDuration(MINS, VALUE2),
                                                new Fare()
                                        )
                                )
                        )
                )
        );
    }
}
