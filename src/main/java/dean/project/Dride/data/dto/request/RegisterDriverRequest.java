package dean.project.Dride.data.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import static dean.project.Dride.utilities.Constants.*;
import static dean.project.Dride.utilities.DriverUrls.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterDriverRequest {

    @NotNull(message = CANNOT_BE_NULL)
    @NotEmpty(message = CANNOT_BE_EMPTY)
    private String name;

    @NotNull(message = CANNOT_BE_NULL)
    @NotEmpty(message = CANNOT_BE_EMPTY)
    @Email(message = MUST_BE_VALID_EMAIL)
    private String email;

    @Size(min = MIN, max = MAX)
    @NotEmpty
    @NotNull
    private String password;
    @NotNull
    @NotBlank
    private String dateOfBirth;

    @NotNull(message = UPLOAD_LICENSE)
    private MultipartFile licenseImage;
}
