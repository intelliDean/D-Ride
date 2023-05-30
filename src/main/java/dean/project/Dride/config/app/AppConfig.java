package dean.project.Dride.config.app;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dean.project.Dride.config.distance.DistanceConfig;
import dean.project.Dride.config.sms.SMSConfig;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.context.Context;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Configuration
public class AppConfig {
    @Value("${cloudinary.cloud.name}")
    private String cloudName;
    @Value("${cloudinary.api.secret}")
    private String apiSecret;
    @Value("${cloudinary.api.key}")
    private String cloudApiKey;
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
    @Value("${twilio.phone.number}")
    private String phoneNumber;
    @Value("${jwt.secret.key}")
    private String jwtSecret;


    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", cloudApiKey,
                        "api_secret", apiSecret));
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
    public WebClient getWebClientBuilder() {
        return WebClient.builder()
                .baseUrl(mailUrl)
                .defaultHeader("api-key", mailApiKey)
                .build();
    }

    @Bean
    public Context context() {
        return new Context();
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
        return new SMSConfig(accountSID, authToken, phoneNumber);
    }


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public Key getSecretKey() {
        return new SecretKeySpec(
                jwtSecret.getBytes(),
                SignatureAlgorithm.HS512.getJcaName());
    }

    @Bean
    public GlobalApiResponse.GlobalApiResponseBuilder getResponse() {
        return GlobalApiResponse.builder();
    }

//    @Bean
//    public Key getSecretKey() {
//        KeyGenerator keyGen;
//        try {
//            keyGen = KeyGenerator.getInstance(SignatureAlgorithm.HS512.getJcaName());     //specify the algorithm
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        SecureRandom random = new SecureRandom();
//        keyGen.init(random); // 512 before
//        return keyGen.generateKey();
//    }

    //    @Bean
//    public TaskExecutor taskExecutor() {
//        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
//        executor.setConcurrencyLimit(10); // Set the maximum number of concurrent threads
//        return executor;
//    }


}
