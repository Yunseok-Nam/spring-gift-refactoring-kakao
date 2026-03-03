package gift.acceptance;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

public class Hooks {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        jdbcTemplate.execute("DELETE FROM orders");
        jdbcTemplate.execute("DELETE FROM wish");
        jdbcTemplate.execute("DELETE FROM options");
        jdbcTemplate.execute("DELETE FROM product");
        jdbcTemplate.execute("DELETE FROM member");
        jdbcTemplate.execute("DELETE FROM category");
    }
}
