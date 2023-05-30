package dean.project.Dride.services.notification;

import dean.project.Dride.data.dto.request.EmailNotificationRequest;
import dean.project.Dride.data.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static dean.project.Dride.exceptions.ExceptionMessage.USER_NOT_FOUND;


@Service
@AllArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final WebClient webClient;

    @Override
    public String sendHTMLMail(EmailNotificationRequest request) {

        return webClient
                .post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public String getName(Long userId) {
        User user = webClient.get()
                .uri("http://localhost:9090/api/v1/user/" + userId)
                .retrieve()
                .bodyToMono(User.class)
                .block();
        if (user != null) {
            return user.getName();
        } else {
            return USER_NOT_FOUND;
        }
    }
}