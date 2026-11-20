package internshipnetwork;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Proves Spring Boot + Flyway + PostgreSQL work together.
 * Needs your local Postgres running with the 'internships' database (see README).
 */
@SpringBootTest
class IntegrationSmokeTest {

    @Autowired JdbcTemplate jdbc;

    @Test
    void contextLoadsAndFlywayBaselineApplied() {
        Integer count = jdbc.queryForObject("SELECT count(*) FROM app_info", Integer.class);
        assertThat(count).isNotNull();
    }
}
