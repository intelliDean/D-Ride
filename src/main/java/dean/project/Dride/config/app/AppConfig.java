package dean.project.Dride.config.app;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.distance.DistanceConfig;
import dean.project.Dride.config.mail.MailConfig;
import dean.project.Dride.config.security.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
     @Value("${cloudinary.cloud.name}")
    private String cloudName;
    @Value("${cloudinary.api.key}")
    private String apiKey;
    @Value("${cloudinary.api.secret}")
    private String apiSecret;

    @Value("${google.distance.url}")
    private String googleDistanceUrl;
    @Value("${google.api.key}")
    private String googleApiKey;

    @Value("${mail.api.key}")
    private String mailApiKey;

    @Value("${sendinblue.mail.url}")
    private String mailUrl;

    @Value("${twilio.account.sid}")
    private String accountSID;
    @Value("${twilio.auth.token}")
    private String authToken;


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret
                )
        );
    }

    @Bean
    public MailConfig mailConfig() {
        return new MailConfig(mailApiKey, mailUrl);
    }


    @Bean
    public DistanceConfig distanceConfig() {
        return new DistanceConfig(googleDistanceUrl, googleApiKey);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return mapper;
    }
    @Bean
    public SMSConfig smsConfig() {
        return new SMSConfig(accountSID, authToken);
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
