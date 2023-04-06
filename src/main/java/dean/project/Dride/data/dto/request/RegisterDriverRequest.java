package dean.project.Dride.data.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import static dean.project.Dride.utilities.DrideUtilities.EMAIL_REGEX_STRING;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterDriverRequest {

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "must be valid email address", regexp = EMAIL_REGEX_STRING)
    private String email;

    @Size(min = 8, max = 20)
    @NotEmpty
    @NotNull
    private String password;
    @NotNull(message = "please upload license image")
    private MultipartFile licenseImage;
}
