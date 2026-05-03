package sia.taco_cloud.tacos.repositories.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sia.taco_cloud.tacos.models.IngredientReference;
import sia.taco_cloud.tacos.models.Taco;
import sia.taco_cloud.tacos.models.TacoOrder;
import sia.taco_cloud.tacos.repositories.OrderRepository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

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

    @Override
    public void saveTacoOrder(TacoOrder order) {
        if(order != null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(insertTacoOrder, new String[]{"id"});
                statement.setString(1, order.getDeliveryName());
                statement.setString(2, order.getDeliveryStreet());
                statement.setString(3, order.getDeliveryCity());
                statement.setString(4, order.getDeliveryState());
                statement.setString(5, order.getDeliveryZip());
                statement.setString(6, order.getCcNumber());
                statement.setString(7, order.getCcExpiration());
                statement.setString(8, order.getCcCvv());
                statement.setTimestamp(9, new java.sql.Timestamp(order.getPlacedAt().getTime()));
            return statement;
        }, keyHolder);
        order.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
            List<Taco> tacos = order.getTacos();
            for(Taco taco : tacos) {
                saveTaco(taco,order.getId());
            }

        } else {
            throw new RuntimeException("Failed to saveTaco order");
        }
    }

    private void saveTaco(Taco taco, long tacoOrderId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(insertTaco, new String[]{"id"});
            statement.setString(1, taco.getName());
            statement.setLong(2, tacoOrderId);
            statement.setTimestamp(3, new java.sql.Timestamp(taco.getCreatedAt().getTime()));
            return statement;
        }, keyHolder);
        long tacoId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        taco.setId(tacoId);
        saveIngredientReference(taco.getIngredients(), tacoId);
    }

    private void saveIngredientReference(List<IngredientReference> ingredients, long tacoId) {
        for(IngredientReference ingredient : ingredients) {
            jdbcTemplate.update(insertIngredientReference, ingredient.getIngredientId(), tacoId);
        }

    }

}
