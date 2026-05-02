package sia.taco_cloud.tacos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

public class QueryConfigTest {
    @Configuration
    @ImportResource("classpath:testQueries.xml") // Fixed path
    public static class TestQueryConfig { // Added static
    }
}