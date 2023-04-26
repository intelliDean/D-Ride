package dean.project.Dride.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import static dean.project.Dride.utilities.PassengerUrls.FULL_NAME;
import static dean.project.Dride.utilities.PassengerUrls.PASSWORD;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterPassengerRequest {
    private String email;
    @JsonProperty(FULL_NAME)
    private String name;
    @JsonProperty(PASSWORD)
    private String password;
}
