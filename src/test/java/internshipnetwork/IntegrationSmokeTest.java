package internshipnetwork;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;

/**
 * Starts the application against PostgreSQL and checks the database and homepage.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationSmokeTest {

    @Autowired
    private JdbcTemplate jdbc;

    @LocalServerPort
    private int port;

    @Test
    void contextLoadsAndFlywayBaselineApplied() {
        Integer count = jdbc.queryForObject("SELECT count(*) FROM app_info", Integer.class);
        assertThat(count).isNotNull();
    }

    @Test
    void homePageRendersOverHttp() {
        ResponseEntity<String> response = RestClient.create()
                .get()
                .uri("http://127.0.0.1:" + port + "/")
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Find Your Next Internship");
    }
}
