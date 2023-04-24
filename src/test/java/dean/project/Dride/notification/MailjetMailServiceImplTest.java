package dean.project.Dride.notification;

import dean.project.Dride.data.dto.request.EmailNotificationRequest;
import dean.project.Dride.data.dto.request.Recipient;
import dean.project.Dride.services.notification.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MailjetMailServiceImplTest {

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
        var response = mailService.sendHTMLMail(emailNotificationRequest);
        assertThat(response).isNotNull();
    }
}