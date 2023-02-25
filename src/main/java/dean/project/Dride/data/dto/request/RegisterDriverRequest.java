package dean.project.Dride.data.dto.request;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterDriverRequest {
    private String name;
    private String email;
    private String password;
    //private MultipartFile licenseImage;
}
