package dean.project.Dride.notification;

import dean.project.Dride.data.dto.request.EmailNotificationRequest;
import dean.project.Dride.data.dto.request.Recipient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SendinBlueMailServiceImplTest {
     @Autowired
    private MailService mailService;
    private EmailNotificationRequest emailNotificationRequest;

    @BeforeEach
    void setUp() {
        List<Recipient> to = List.of(
                new Recipient("dean", "bonel60110@orgria.com")
        );
        emailNotificationRequest=new EmailNotificationRequest();
        emailNotificationRequest.setTo(to);
        emailNotificationRequest.setHtmlContent("Hello");
    }

    @Test
    void sendHtmlMail() {
        var response = mailService.sendHtmlMail(emailNotificationRequest);
        assertThat(response).isNotNull();
    }

}