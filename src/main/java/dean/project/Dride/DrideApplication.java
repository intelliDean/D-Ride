package dean.project.Dride;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Dride API",
                version = "1.0",
                description = "This app provides REST APIs documentation for DrideApp",
                contact = @Contact(
                        name = "Dride Support",
                        email = "o.michaeldean@gmail.com",
                        url = "https://github.com/intelliDean"
                )
        ),
        servers = {
                @Server(
                        description = "dev",
                        url = "http://localhost:9090"
                ),
                @Server(
                        description = "test",
                        url = "http://localhost:8181"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "Bearer"
                )
        }
) @SecurityScheme(
        name = "Bearer",
        description = "JWT Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class DrideApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrideApplication.class, args);
        log.info("::::: Dride Server Running :::::");
    }
}