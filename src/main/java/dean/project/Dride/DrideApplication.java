package dean.project.Dride;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "DrideApp API",
                version = "v1",
                description = "This app provides REST APIs documentation for DrideApp",
                contact = @Contact(
                        name = "Dride Support",
                        email = "noreply@dride.org"
                )
        )
)
public class DrideApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrideApplication.class, args);
        log.info("::::: Dride Server Running :::::");
    }
}