package dean.project.Dride.data.dto.response.google_dtos;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DistanceMatrixElement {
    private DistanceMatrixElementStatus status;

    private GoogleDistance distance;

    private GoogleDuration duration;
    private Fare fare;

}
