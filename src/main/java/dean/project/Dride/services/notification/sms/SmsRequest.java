package dean.project.Dride.services.notification.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SmsRequest {
    @JsonProperty("phone_number")
    @NotBlank
    private String recipientPhoneNumber;
    @JsonProperty("message")
    @NotBlank
    private String message;
}