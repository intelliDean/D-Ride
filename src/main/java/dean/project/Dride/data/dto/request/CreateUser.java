package dean.project.Dride.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static dean.project.Dride.utilities.DriverUrls.*;
import static dean.project.Dride.utilities.PassengerUrls.FULL_NAME;
import static dean.project.Dride.utilities.PassengerUrls.PASSWORD;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateUser {
    @NotNull(message = CANNOT_BE_NULL)
    @NotEmpty(message = CANNOT_BE_EMPTY)
    @JsonProperty(FULL_NAME)
    private String name;

    @NotNull(message = CANNOT_BE_NULL)
    @NotEmpty(message = CANNOT_BE_EMPTY)
    @Email(message = MUST_BE_VALID_EMAIL)
    private String email;

    @NotNull(message = CANNOT_BE_NULL)
    @NotEmpty(message = CANNOT_BE_EMPTY)
    @Size(min = MIN, max = MAX)
    @JsonProperty(PASSWORD)
    private String password;
}
