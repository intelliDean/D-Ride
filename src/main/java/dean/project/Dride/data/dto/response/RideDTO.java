package dean.project.Dride.data.dto.response;

import dean.project.Dride.data.models.Driver;
import dean.project.Dride.data.models.Passenger;
import dean.project.Dride.data.models.Rating;
import dean.project.Dride.data.models.Status;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RideDTO {
    private Passenger passenger;
    private Driver driver;
    private String origin;
    private String destination;
    private Status rideStatus;
    private String startTime;
    private String endTime;
    private String bookTime;
    private Rating driverRating;
    private Rating passengerRating;
    private BigDecimal fare;
}
