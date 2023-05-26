package dean.project.Dride.data.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {
//    @NotNull(message.html = CANNOT_BE_NULL)
//    @NotEmpty(message.html = CANNOT_BE_EMPTY)
//    @JsonProperty(FULL_NAME)
//    private String name;
//
//    @NotNull(message.html = CANNOT_BE_NULL)
//    @NotEmpty(message.html = CANNOT_BE_EMPTY)
//    @Email(message.html = MUST_BE_VALID_EMAIL)
//    private String email;
//
//    @NotNull(message.html = CANNOT_BE_NULL)
//    @NotEmpty(message.html = CANNOT_BE_EMPTY)
//    @Size(min = MIN, max = MAX)
//    @JsonProperty(PASSWORD)
//    private String password;
    private CreateUser createUser;
    @NotNull
    @NotBlank
    private String dateOfBirth;
    private String licenseNumber;

    @NotNull(message = "Please upload license image")
    private MultipartFile licenseImage;
}
