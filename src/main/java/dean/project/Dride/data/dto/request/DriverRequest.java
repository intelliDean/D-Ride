package dean.project.Dride.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DriverRequest {
    private CreateUser createUser;
    @NotNull
    @NotBlank
    private String dateOfBirth;
    @NotNull
    @NotBlank
    private String licenseNumber;
    @NotNull
    @NotBlank
    private String token;

    @NotNull(message = "Please upload license image")
    private MultipartFile licenseImage;
}
