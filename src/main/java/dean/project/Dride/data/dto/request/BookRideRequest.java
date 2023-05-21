package dean.project.Dride.data.dto.request;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookRideRequest {
    private Location origin;
    private Location destination;
}
