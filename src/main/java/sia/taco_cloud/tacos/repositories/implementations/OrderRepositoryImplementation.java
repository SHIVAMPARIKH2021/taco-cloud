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

    public OrderRepositoryImplementation(JdbcTemplate jdbcTemplate,
                                         String insertTacoOrder,
                                         String insertIngredientReference,
                                         String insertTaco) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertTacoOrder = insertTacoOrder;
        this.insertIngredientReference = insertIngredientReference;
        this.insertTaco = insertTaco;
    }

    public TacoOrder save(TacoOrder order) {
        jdbcTemplate.update(insertTacoOrder);
        List<Taco> tacos = order.getTacos();
        for(Taco taco : tacos) {
            saveTaco(taco);
        }
        return order;
    }

    private void saveTaco(Taco taco) {
        jdbcTemplate.update(insertTaco);
        saveIngredientReference(taco.getId(), taco.getIngredients());
    }

    private void saveIngredientReference(long tacoId, List<Ingredient> ingredients) {
        for(Ingredient ingredient : ingredients) {
            jdbcTemplate.update(insertIngredientReference, ingredient.getId(), tacoId);
        }

    }
}
