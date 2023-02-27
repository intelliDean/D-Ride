package dean.project.Dride.data.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterRequest {
    private String name;
    private String email;
    private String password;
}
