package dean.project.Dride.data.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AcceptRideRequest {
    private Long passengerId;
    private Long driverId;
}
