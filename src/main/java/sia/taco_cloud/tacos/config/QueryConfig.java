package sia.taco_cloud.tacos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:queries.xml")
public class QueryConfig {
}
