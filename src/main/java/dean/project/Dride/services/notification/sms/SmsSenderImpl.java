package dean.project.Dride.services.notification.sms;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("twilio")
@AllArgsConstructor
public class SmsSenderImpl implements SmsSender {
    private final TwilioConfig config;
    private final static Logger LOGGER = LoggerFactory.getLogger(TwilioConfig.class);

    @Override
    public void sendSms(SmsRequest request) {
        PhoneNumber to = new PhoneNumber(request.getRecipientPhoneNumber());
        PhoneNumber from = new PhoneNumber(config.getTwilioPhoneNumber());
        MessageCreator creator = Message.creator(
                to,
                from,
                request.getMessage());
        creator.create();
        LOGGER.info("sms sent{}", request);
    }
}
