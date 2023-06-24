package dean.project.Dride.services.notification.sms;

import com.twilio.Twilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioInitializer {
    private final static Logger LOGGER = LoggerFactory.getLogger(TwilioInitializer.class);

    @Autowired
    public TwilioInitializer(TwilioConfig config) {
        Twilio.init(
                config.getTwilioSid(),
                config.getTwilioToken()
        );
        LOGGER.info(":::::::::::::Twilio Initialization::::::::::::");
    }

}
