package dean.project.Dride.data.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AcceptRideResponse {
    private Long rideId;
    private String name;
    private String phoneNumber;
    private String profileImage;
}
