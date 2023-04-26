package dean.project.Dride.data.dto.response.api_response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalApiResponse {
    private Long id;
    private String message;
    private String estimatedTimeOfArrival;
    private BigDecimal fare;
    private String name;
    private String phoneNumber;
    private String profileImage;
    private Long employeeId;
}
