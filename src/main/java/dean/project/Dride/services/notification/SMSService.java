package dean.project.Dride.services.notification;


import dean.project.Dride.data.dto.request.SMSNotification;

public interface SMSService {
    String sendSMS(SMSNotification notification);
}
