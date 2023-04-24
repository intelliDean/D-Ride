package dean.project.Dride.services.notification;


import dean.project.Dride.data.dto.request.EmailNotificationRequest;

public interface MailService {
    //String sendHtmlMail(EmailNotificationRequest request);
    String sendHTMLMail(EmailNotificationRequest request);
    String getName(Long userId);
}
