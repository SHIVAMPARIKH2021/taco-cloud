package sia.taco_cloud.tacos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

public class QueryConfigTest {
    @Configuration
    @ImportResource("classpath:src/test/resources/testQueries.xml")
    public class TestQueryConfig {
    }
}
