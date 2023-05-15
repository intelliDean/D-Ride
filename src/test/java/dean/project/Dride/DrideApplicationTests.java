package dean.project.Dride;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DrideApplicationTests {

   @Test
	void contextLoads() {
	}

	@Test
	void testDatabaseConnection(){
		DriverManagerDataSource dataSource =
				new DriverManagerDataSource("jdbc:h2:mem://127.0.0.1:9092");
		try {
			Connection connection = dataSource.getConnection("root", "password");
			System.out.println(connection);
			assertThat(connection).isNotNull();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
