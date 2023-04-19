package dean.project.Dride.config.sms;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class SMSConfig {
    private String accountSID;
    private String authToken;
    private String phoneNumber;


}
