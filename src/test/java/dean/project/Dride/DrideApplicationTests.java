package dean.project.Dride;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootTest
class DrideApplicationTests {

    @Test
    void contextLoads() {
    }
	@Test
	void testThatWeConnectWithDatabase() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:mysql://localhost:3306", "root", "@Tiptop2059!");
	}
}
