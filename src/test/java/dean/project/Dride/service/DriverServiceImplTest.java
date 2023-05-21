package dean.project.Dride.service;

import dean.project.Dride.data.dto.request.RegisterRequest;
import dean.project.Dride.services.driver_service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.IOException;

import static dean.project.Dride.utilities.Constants.UBER_DELUXE_TEST_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Slf4j
class DriverServiceImplTest {

    @Autowired
    private DriverService driverService;
    private RegisterRequest request;
    @BeforeEach
    void setUp() {
        request=new RegisterRequest();
        request.getCreateUser().setPassword("test_password");
        request.getCreateUser().setName("test driver");
        request.getCreateUser().setEmail("test@email.com");
    }

    @Test
    void register() throws IOException {
        MockMultipartFile file =
                new MockMultipartFile("test_license",
                        new FileInputStream(UBER_DELUXE_TEST_IMAGE));
        request.setLicenseImage(file);
        var response = driverService.register(request);
        assertThat(response).isNotNull();
        //assertThat(response.isSuccess()).isTrue();

    }
}