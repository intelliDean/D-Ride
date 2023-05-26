package dean.project.Dride.services.user_service.utility;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity <T> {
    private T user;
}
