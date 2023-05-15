package dean.project.Dride.data.dto.request;

import com.twilio.type.PhoneNumber;

public record TwilioInfo(
        PhoneNumber to,
        PhoneNumber from,
        String message) {

}