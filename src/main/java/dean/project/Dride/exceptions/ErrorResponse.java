package dean.project.Dride.exceptions;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private Integer statusCode;
    private String message;
    private String timeStamp;
}
