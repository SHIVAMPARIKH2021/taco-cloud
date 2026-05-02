package sia.taco_cloud.tacos.repositories.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sia.taco_cloud.tacos.config.QueryConfigTest;
import sia.taco_cloud.tacos.constants.Ingredient;
import sia.taco_cloud.tacos.models.Taco;
import sia.taco_cloud.tacos.models.TacoOrder;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class OrderRepositoryImplementationTest {

    private JdbcTemplate jdbcTemplate;

    private OrderRepositoryImplementation repository;

    @Autowired
    private String insertTacoOrderTest;

    @Autowired
    private String insertIngredientReferenceTest;

    @Autowired
    private String insertTacoTest;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new OrderRepositoryImplementation(
                jdbcTemplate,
                insertTacoOrderTest,
                insertIngredientReferenceTest,
                insertTacoTest
        );
    }

    @Test
    void testSaveOrder_ShouldExecuteUpdateQueries() {
        // 1. Arrange: Create an order with one taco and two ingredients
        TacoOrder order = new TacoOrder();

        Taco taco = new Taco();
        taco.setId(1L);

        Ingredient i1 = new Ingredient("ING1", "Name1", Ingredient.Type.SAUCE);
        Ingredient i2 = new Ingredient("ING2", "Name2", Ingredient.Type.CHEESE);
        taco.setIngredients(Arrays.asList(i1, i2));

        order.setTacos(Arrays.asList(taco));

        // 2. Act
        repository.save(order);

        // 3. Assert: Verify the order was saved
        verify(jdbcTemplate, times(2)).update(eq(insertTacoOrderTest));

        // Verify the taco was saved
        verify(jdbcTemplate, times(2)).update(eq(insertTacoTest));

        // Verify ingredients were linked (called twice for two ingredients)
        verify(jdbcTemplate, times(1)).update(eq(insertIngredientReferenceTest), eq("ING1"), eq(1L));
        verify(jdbcTemplate, times(1)).update(eq(insertIngredientReferenceTest), eq("ING2"), eq(1L));
    }
}