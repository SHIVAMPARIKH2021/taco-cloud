package sia.taco_cloud.tacos.repositories.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sia.taco_cloud.tacos.config.QueryConfigTest;
import sia.taco_cloud.tacos.models.IngredientReference;
import sia.taco_cloud.tacos.models.Taco;
import sia.taco_cloud.tacos.models.TacoOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                insertTacoTest// This should be a mock or a real instance depending on your test design
        );
    }

    @Test
    void testSaveTacoOrderTacoOrderOrder_ShouldExecuteUpdateQueries() {
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

        IngredientReference i1 = new IngredientReference("ING1");
        taco.setIngredients(List.of(i1));
        order.setTacos(List.of(taco));

        // Mocking the Order Insert (KeyHolder logic)
        doAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1); // Get the KeyHolder passed to update
            kh.getKeyList().add(java.util.Map.of("id", 100L)); // Simulate DB returning ID 100
            return 1; // Return row count 1
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        // CRITICAL: Mock the behavior for row count and ID retrieval
        when(jdbcTemplate.update(eq(insertTacoOrderTest), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(1);
        when(jdbcTemplate.queryForObject(eq(getTacoOrderTest), eq(Long.class)))
                .thenReturn(100L); // Mocking that the DB generated ID 100

        repository.saveTacoOrder(order);

        assertEquals(100L, order.getId());

        verify(jdbcTemplate, atLeastOnce()).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        verify(jdbcTemplate).update(eq(insertIngredientReferenceTest), eq("ING1"), anyLong());
    }
}