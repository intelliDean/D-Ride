package dean.project.Dride.data.dto.response.entity_dtos;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dean.project.Dride.data.models.Gender;
import dean.project.Dride.data.models.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PassengerDTO {
    @JsonUnwrapped
    private User user;
    private String phoneNumber;
    private Gender gender;
}
