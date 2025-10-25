package sia.taco_cloud;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import sia.taco_cloud.tacos.repositories.implementations.IngredientRepositoryImplementation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest
@Import(TacoCloudApplicationTests.TestConfig.class)
class TacoCloudApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Configuration
    static class TestConfig {
        @Bean
        IngredientRepositoryImplementation ingredientRepositoryImplementation() {
            return Mockito.mock(IngredientRepositoryImplementation.class);
        }

        // small test controller to register the "/" mapping for the slice test
        @Controller
        static class TestHomeController {
            @GetMapping("/")
            String home() {
                return "home";
            }
        }
    }

	@Test
	public void testHomepage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
	}

}
