package dean.project.Dride.data.dto.request;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminDetailsRequest {
    private Long adminId;
    private String password;
}
