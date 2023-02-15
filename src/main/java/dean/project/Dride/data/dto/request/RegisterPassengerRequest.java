package dean.project.Dride.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegisterPassengerRequest {
    private String email;
    @JsonProperty("full_name")
    private String name;

    private String password;
}
