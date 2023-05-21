package dean.project.Dride.data.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import static dean.project.Dride.utilities.DriverUrls.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {
//    @NotNull(message = CANNOT_BE_NULL)
//    @NotEmpty(message = CANNOT_BE_EMPTY)
//    @JsonProperty(FULL_NAME)
//    private String name;
//
//    @NotNull(message = CANNOT_BE_NULL)
//    @NotEmpty(message = CANNOT_BE_EMPTY)
//    @Email(message = MUST_BE_VALID_EMAIL)
//    private String email;
//
//    @NotNull(message = CANNOT_BE_NULL)
//    @NotEmpty(message = CANNOT_BE_EMPTY)
//    @Size(min = MIN, max = MAX)
//    @JsonProperty(PASSWORD)
//    private String password;
    private CreateUser createUser;
    @NotNull
    @NotBlank
    private String dateOfBirth;
    private String licenseNumber;

    @NotNull(message = UPLOAD_LICENSE)
    private MultipartFile licenseImage;
}
