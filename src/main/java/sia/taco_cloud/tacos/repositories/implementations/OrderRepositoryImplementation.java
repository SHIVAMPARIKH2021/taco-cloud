package sia.taco_cloud.tacos.repositories.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sia.taco_cloud.tacos.constants.Ingredient;
import sia.taco_cloud.tacos.models.Taco;
import sia.taco_cloud.tacos.models.TacoOrder;
import sia.taco_cloud.tacos.repositories.OrderRepository;

import java.util.List;

@Repository
public class OrderRepositoryImplementation implements OrderRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String insertTacoOrder;

    @Autowired
    private String insertIngredientReference;

    @Autowired
    private String insertTaco;

    @Autowired
    private String getTacoOrder;

    @Autowired
    private OrderRepository orderRepository;

    public OrderRepositoryImplementation(JdbcTemplate jdbcTemplate,
                                         String insertTacoOrder,
                                         String insertIngredientReference,
                                         String insertTaco,
                                         String getTacoOrder) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertTacoOrder = insertTacoOrder;
        this.insertIngredientReference = insertIngredientReference;
        this.insertTaco = insertTaco;
        this.getTacoOrder = getTacoOrder;
    }

    public TacoOrder save(TacoOrder order) {
        long tacoId = 0;
        int rowCount = jdbcTemplate.update(insertTacoOrder,
                order.getDeliveryName(),
                order.getDeliveryStreet(),
                order.getDeliveryCity(),
                order.getDeliveryState(),
                order.getDeliveryZip(),
                order.getCcNumber(),
                order.getCcExpiration(),
                order.getCcCvv(),
                order.getPlacedAt());
        if (rowCount == 1) {
            long tacoOrderId = jdbcTemplate.queryForObject(getTacoOrder, Long.class);
            order.setId(tacoOrderId);
            List<Taco> tacos = order.getTacos();
            for(Taco taco : tacos) {
                taco.setTacoOrderId(tacoOrderId);
                tacoId++;
                saveTaco(taco,tacoOrderId, tacoId);
            }
            return order;
        }
        else {
            throw new RuntimeException("Failed to save order");
        }
    }

    private void saveTaco(Taco taco, long tacoOrderId, long tacoId) {
        jdbcTemplate.update(insertTaco, taco.getName(), tacoOrderId, tacoId);
        saveIngredientReference(taco.getIngredients(), taco.getId());
    }

    private void saveIngredientReference(List<Ingredient> ingredients, long tacoId) {
        for(Ingredient ingredient : ingredients) {
            jdbcTemplate.update(insertIngredientReference, ingredient.getId(), tacoId);
        }

    }
}
