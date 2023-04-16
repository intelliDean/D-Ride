package dean.project.Dride.data.dto.request;

import dean.project.Dride.data.models.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {
    private Long passengerId;  // I won't use this anymore when i implement the currentUser();
    private Location origin;
    private Location destination;
    private BigDecimal fare;
    private Status rideStatus;
}
