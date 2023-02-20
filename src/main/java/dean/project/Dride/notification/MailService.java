package dean.project.Dride.notification;

import dean.project.Dride.data.dto.request.EmailNotificationRequest;

public interface MailService {
    String sendHtmlMail(EmailNotificationRequest request);
}
