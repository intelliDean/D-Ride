package dean.project.Dride.services.notification;

import dean.project.Dride.config.mail.MailConfig;
import dean.project.Dride.data.dto.request.EmailNotificationRequest;
import dean.project.Dride.data.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


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
        headers.set("api-key", apiKey);

        return webClient.post()
                .uri(url)
                .headers(header -> header.addAll(headers))
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
//    public String sendHtmlMail(EmailNotificationRequest request) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = mailConfig.getMailUrl();
//        String apiKey = mailConfig.getApiKey();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("api-key", apiKey);
//        HttpEntity<EmailNotificationRequest> requestEntity = new HttpEntity<>(request, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
//        //log.info("res->{}", response);
//        return response.getBody();
//
//    }

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
            return "User could not be found";
        }
    }
}