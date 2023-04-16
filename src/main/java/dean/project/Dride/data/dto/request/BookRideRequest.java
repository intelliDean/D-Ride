package dean.project.Dride.data.dto.request;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookRideRequest {
    private Long passengerId;
    private Location origin;
    private Location destination;
}
