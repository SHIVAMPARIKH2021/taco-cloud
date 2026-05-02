package sia.taco_cloud.tacos.repositories.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sia.taco_cloud.tacos.constants.Ingredient;
import sia.taco_cloud.tacos.models.IngredientReference;
import sia.taco_cloud.tacos.models.Taco;
import sia.taco_cloud.tacos.models.TacoOrder;
import sia.taco_cloud.tacos.repositories.OrderRepository;

import java.util.List;
import java.util.Set;

@Repository
public class OrderRepositoryImplementation {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private String insertTacoOrder;

    @Autowired
    private String insertIngredientReference;

    @Autowired
    private String insertTaco;

    private OrderRepository orderRepository;

    public OrderRepositoryImplementation(JdbcTemplate jdbcTemplate,
                                         String insertTacoOrder,
                                         String insertIngredientReference,
                                         String insertTaco,
                                         OrderRepository orderRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertTacoOrder = insertTacoOrder;
        this.insertIngredientReference = insertIngredientReference;
        this.insertTaco = insertTaco;
        this.orderRepository = orderRepository;
    }

    public void saveTacoOrder(TacoOrder order) {
        long tacoId = 0;
        if(order != null) {
        orderRepository.save(order);
            Set<Taco> tacos = order.getTacos();
            for(Taco taco : tacos) {
                tacoId++;
                saveTaco(taco,order.getId(), tacoId);
            }

        } else {
            throw new RuntimeException("Failed to saveTaco order");
        }
    }

    private void saveTaco(Taco taco, long tacoOrderId, long tacoId) {
        jdbcTemplate.update(insertTaco, taco.getName(), tacoOrderId, tacoId);
        saveIngredientReference(taco.getIngredients(), taco.getId());
    }

    private void saveIngredientReference(Set<IngredientReference> ingredients, long tacoId) {
        for(IngredientReference ingredient : ingredients) {
            jdbcTemplate.update(insertIngredientReference, ingredient.getIngredientId(), tacoId);
        }

    }
}
