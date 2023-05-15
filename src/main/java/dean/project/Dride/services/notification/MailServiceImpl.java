package dean.project.Dride.services.notification;

import dean.project.Dride.config.mail.MailConfig;
import dean.project.Dride.data.dto.request.EmailNotificationRequest;
import dean.project.Dride.data.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import static dean.project.Dride.exceptions.ExceptionMessage.USER_NOT_FOUND;
import static dean.project.Dride.utilities.Constants.API_KEY;


@Service
@AllArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final MailConfig mailConfig;
    private final WebClient webClient;

    @Override
    public String sendHTMLMail(EmailNotificationRequest request) {
        String url = mailConfig.getMailUrl();
        String apiKey = mailConfig.getApiKey();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(API_KEY, apiKey);  //"api-key"
        HttpEntity<EmailNotificationRequest> requestEntity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        return response.getBody();
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
//    @Override
//    public String sendHTMLMail(EmailNotificationRequest request) {
//        String url = mailConfig.getMailUrl();
//        String apiKey = mailConfig.getApiKey();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set(API_KEY, apiKey);   //"api_key"
//
//        return webClient.post()
//                .uri(url)
//                .headers(header -> header.addAll(headers))
//                .body(BodyInserters.fromValue(request))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }
}