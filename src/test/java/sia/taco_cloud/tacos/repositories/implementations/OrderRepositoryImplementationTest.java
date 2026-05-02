package sia.taco_cloud.tacos.repositories.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = QueryConfigTest.TestQueryConfig.class)
public class OrderRepositoryImplementationTest {

    private JdbcTemplate jdbcTemplate;
    private OrderRepositoryImplementation repository;

    @Autowired
    @Qualifier("insertTacoOrderTest")
    private String insertTacoOrderTest;

    @Autowired
    @Qualifier("insertIngredientReferenceTest")
    private String insertIngredientReferenceTest;

    @Autowired
    @Qualifier("insertTacoTest")
    private String insertTacoTest;

    @Autowired
    @Qualifier("getTacoOrderTest") // Ensure this exists in your XML
    private String getTacoOrderTest;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new OrderRepositoryImplementation(
                jdbcTemplate,
                insertTacoOrderTest,
                insertIngredientReferenceTest,
                insertTacoTest,
                getTacoOrderTest
        );
    }

    @Test
    void testSaveOrder_ShouldExecuteUpdateQueries() {
        // 1. Arrange
        TacoOrder order = new TacoOrder();
        order.setDeliveryName("John Doe");
        order.setDeliveryStreet("123 Main St");
        order.setDeliveryCity("Anytown");
        order.setDeliveryState("CA");
        order.setDeliveryZip("12345");
        order.setCcNumber("4111111111111111");
        order.setCcExpiration("12/25");
        order.setCcCvv("123");
        order.setPlacedAt(new java.util.Date());

        Taco taco = new Taco();
        taco.setId(1L); // This represents the ID used for ingredient references
        taco.setName("Test Taco");

        Ingredient i1 = new Ingredient("ING1", "Name1", Ingredient.Type.SAUCE);
        taco.setIngredients(Arrays.asList(i1));
        order.setTacos(Arrays.asList(taco));

        // CRITICAL: Mock the behavior for row count and ID retrieval
        when(jdbcTemplate.update(eq(insertTacoOrderTest), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(1);
        when(jdbcTemplate.queryForObject(eq(getTacoOrderTest), eq(Long.class)))
                .thenReturn(100L); // Mocking that the DB generated ID 100

        // 2. Act
        repository.save(order);

        // 3. Assert

        // Verify Order Insert
        verify(jdbcTemplate, times(1)).update(
                eq(insertTacoOrderTest),
                eq("John Doe"), eq("123 Main St"), eq("Anytown"), eq("CA"), eq("12345"),
                eq("4111111111111111"), eq("12/25"), eq("123"),
                eq(order.getPlacedAt())
        );

        // Verify ID Retrieval
        verify(jdbcTemplate, times(1)).queryForObject(eq(getTacoOrderTest), eq(Long.class));

        // Verify Taco Insert (Matches: name, tacoOrderId, tacoId)
        // Based on your code: tacoOrderId is 100L, and tacoId starts at 0 and increments to 1
        verify(jdbcTemplate, times(1)).update(
                eq(insertTacoTest),
                eq("Test Taco"),
                eq(100L),
                eq(1L)
        );

        // Verify Ingredient Reference
        // Note: Your code passes taco.getId() which we set to 1L in Arrange
        verify(jdbcTemplate, times(1)).update(
                eq(insertIngredientReferenceTest),
                eq("ING1"),
                eq(1L)
        );
    }
}