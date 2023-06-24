package dean.project.Dride.services.notification.sms;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twilio")
@Getter
@Setter
public class TwilioConfig {
    @Value("${twilio.number}")
    private String twilioPhoneNumber;
    @Value("${twilio.sid}")
    private String twilioSid;
    @Value("${twilio.token}")
    private String twilioToken;
}
