package dean.project.Dride;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import static dean.project.Dride.utilities.Constants.DRIDE_EMAIL;

@Slf4j

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "DrideApp API",
                version = "v1",
                description = "This app provides REST APIs documentation for DrideApp",
                contact = @Contact(
                        name = "Dride Support",
                        email = DRIDE_EMAIL
                )
        )
)
public class DrideApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrideApplication.class, args);
        log.info("::::: Dride Server Running :::::");
    }
}