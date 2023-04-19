package dean.project.Dride.services.notification;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import dean.project.Dride.config.sms.SMSConfig;
import com.twilio.type.PhoneNumber;
import dean.project.Dride.data.dto.request.SMSNotification;
import dean.project.Dride.data.dto.request.TwilioInfo;
import dean.project.Dride.exceptions.DrideException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.DecimalFormat;

@AllArgsConstructor
@Service
public class SMSServiceImpl implements SMSService {
    private final SMSConfig smsConfig;

    @Override
    public String sendSMS(SMSNotification notification) {
        try {
            Twilio.init(smsConfig.getAccountSID(), smsConfig.getAuthToken());
            String body = notification.getMessage() + "whatever you put here";
            TwilioInfo smsInfo = new TwilioInfo(
                    new PhoneNumber(notification.getReceiverNumber()),
                    new PhoneNumber(smsConfig.getPhoneNumber()),
                    body);
            Message message = Message.creator(smsInfo.to(), smsInfo.from(), body).create();
            return message.getSid();
        } catch (Exception e) {
            throw  new DrideException("SMS sending failed");
        }
    }

    private String generateOTP() {
        return new DecimalFormat("00000")
                .format(new SecureRandom().nextInt(99999));
    }



}