package dean.project.Dride.data.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SMSNotification {
    private String receiverName;
    private String receiverNumber;
    private String message;
}
