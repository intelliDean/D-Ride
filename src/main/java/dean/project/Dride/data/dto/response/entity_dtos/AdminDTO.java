package dean.project.Dride.data.dto.response.entity_dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dean.project.Dride.data.models.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminDTO {
    @JsonUnwrapped
    private User user;
    private String employeeId;
}
