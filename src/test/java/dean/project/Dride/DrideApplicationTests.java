package dean.project.Dride;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static dean.project.Dride.utilities.DrideUtilities.DB_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DrideApplicationTests {

   @Test
	void contextLoads() {
	}

	@Test
	void testDatabaseConnection(){
		DriverManagerDataSource dataSource =
				new DriverManagerDataSource("jdbc:msql://127.0.0.1:3306");
		try {
			Connection connection = dataSource.getConnection("root", DB_PASSWORD);
			System.out.println(connection);
			assertThat(connection).isNotNull();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
