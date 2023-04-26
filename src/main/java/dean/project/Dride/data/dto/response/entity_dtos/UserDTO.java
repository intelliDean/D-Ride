package dean.project.Dride.data.dto.response.entity_dtos;

import dean.project.Dride.data.models.Role;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {
    private String name;
    private String password;
    private String email;
    private String profileImage;
    private Set<Role> roles;
    private String createdAt;
    private Boolean isEnabled;
}
