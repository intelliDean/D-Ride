package dean.project.Dride.data.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RegisterResponse {
    private Long id;
    private String message;
    private int code;
    private boolean isSuccessful;
}
