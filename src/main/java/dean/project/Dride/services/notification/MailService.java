package dean.project.Dride.services.notification;


import dean.project.Dride.data.dto.request.EmailNotificationRequest;
//import reactor.core.publisher.Mono;

public interface MailService {
    String sendHTMLMail(EmailNotificationRequest request);
    //String sendHTMLMail(EmailNotificationRequest request);
    String getName(Long userId);
}
