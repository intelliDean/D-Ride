package dean.project.Dride.services.notification;

import dean.project.Dride.config.mail.MailConfig;
import dean.project.Dride.data.dto.request.EmailNotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@AllArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final MailConfig mailConfig;


    @Override
    public String sendHtmlMail(EmailNotificationRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", mailConfig.getApiKey());
        HttpEntity<EmailNotificationRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(mailConfig.getMailUrl(), requestEntity, String.class);
        //log.info("res->{}", response);
        return response.getBody();

    }
//
//    public String sendMail() {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://example.com/api/resource";
//        String requestBody = "{ \"name\": \"John Doe\", \"age\": 30 }";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//
//        System.out.println("Response status code: " + response.getStatusCode());
//        System.out.println("Response body: " + response.getBody());
//        re
//    }

}


