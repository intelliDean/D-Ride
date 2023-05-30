package dean.project.Dride.data.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterDriverRequest {

    @NotNull(message = "")
    @NotEmpty(message = "")
    private String name;

    @NotNull(message = "")
    @NotEmpty(message = "")
    @Email(message = "")
    private String email;

    @Size(min = 8, max = 20)
    @NotEmpty
    @NotNull
    private String password;
    @NotNull
    @NotBlank
    private String dateOfBirth;
    private String licenseNumber;

    @NotNull(message = "")
    private MultipartFile licenseImage;
}
