package dean.project.Dride.config.sms;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SMSConfig {
    private String accountSID;
    private String authToken;
}
