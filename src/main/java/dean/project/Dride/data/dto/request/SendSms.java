package dean.project.Dride.data.dto.request;

import com.twilio.type.PhoneNumber;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SendSms {
    private PhoneNumber from;
    private PhoneNumber to;
    private String body;
}
