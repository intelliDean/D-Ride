package dean.project.Dride.data.dto.response.google_dtos;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DistanceMatrixRow {
    private List<DistanceMatrixElement> elements;
}
