package dean.project.Dride.data.repositories;

import dean.project.Dride.data.models.Driver;
import dean.project.Dride.data.models.DriverLicense;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class DriverRepositoryTest {

    @Mock
    private DriverRepository driverRepository;

    @Test
    void findDriverByUser_Id() throws IOException {
        Driver driver = Mockito.mock(Driver.class);
        MockMultipartFile file = new MockMultipartFile(
                "driver_license",
                new FileInputStream("file path")
        );

        when(driverRepository.save(driver)).thenReturn(driver);
//        when(driverRepository.findDriverByUser_Id(driver.getId())
//        ).thenReturn(Optional.of(driver));
//
    }

    @Test
    void existsByUserEmail() {
    }
}