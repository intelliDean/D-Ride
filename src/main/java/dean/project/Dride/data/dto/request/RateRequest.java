package dean.project.Dride.data.dto.request;

import dean.project.Dride.data.models.Rating;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RateRequest {
    private Long rateeId;
    private Rating rating;
}
