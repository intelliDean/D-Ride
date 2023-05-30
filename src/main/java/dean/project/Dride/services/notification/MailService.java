package dean.project.Dride.services.notification;


//import reactor.core.publisher.Mono;

import dean.project.Dride.data.dto.request.EmailNotificationRequest;

public interface MailService {
    String sendHTMLMail(EmailNotificationRequest request);
    //String sendHTMLMail(EmailNotificationRequest request);
    String getName(Long userId);
}
