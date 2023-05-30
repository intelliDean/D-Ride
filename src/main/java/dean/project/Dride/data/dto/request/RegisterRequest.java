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
public class RegisterRequest {
    private CreateUser createUser;
    @NotNull
    @NotBlank
    private String dateOfBirth;
    private String licenseNumber;

    @NotNull(message = "Please upload license image")
    private MultipartFile licenseImage;
}
