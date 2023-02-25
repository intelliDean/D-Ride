package dean.project.Dride.data.dto.response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ApiResponse {
    private int status;
    private String message;
}
