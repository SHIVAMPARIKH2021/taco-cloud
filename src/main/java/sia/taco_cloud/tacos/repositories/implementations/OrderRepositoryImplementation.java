package sia.taco_cloud.tacos.repositories.implementations;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import sia.taco_cloud.tacos.constants.Ingredient;
import sia.taco_cloud.tacos.models.IngredientReference;
import sia.taco_cloud.tacos.models.Taco;
import sia.taco_cloud.tacos.models.TacoOrder;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class OrderRepositoryImplementation {

    private JdbcOperations jdbcOperations;

    public OrderRepositoryImplementation(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    public TacoOrder save(TacoOrder order) {
        // Implementation to save the TacoOrder to the database
        // This is a placeholder implementation
        String sql = "INSERT INTO taco_order (name, street, city, state, zip, cc_number, cc_expiration, cc_cvv, placed_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sql, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.TIMESTAMP);

        pscf.setReturnGeneratedKeys(true);
        order.setPlacedAt(new Date());

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(order.getDeliveryName(), order.getDeliveryStreet(),
                order.getDeliveryCity(), order.getDeliveryState(), order.getDeliveryZip(),
                order.getCcNumber(), order.getCcExpiration(), order.getCcCvv(), order.getPlacedAt()));

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);

        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);

        List<Taco> tacos = order.getTacos();
        int i = 0;
        for(Taco taco : tacos) {
            saveTaco(taco, orderId, i++);
        }
        return order;
    }

    private long saveTaco(Taco taco, long orderId, int orderKey) {
        String sql = "INSERT INTO taco (taco_order, taco_order_id, name, created_at)"
                 + "VALUES (?, ?, ?, ?)";

        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sql,
                Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.TIMESTAMP);
        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(orderId, orderKey, taco.getName(), new Date())
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);

        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        saveIngredientReference(tacoId, taco.getIngredients());
        return tacoId;
    }

    private void saveIngredientReference(long tacoId, List<Ingredient> ingredients) {
        int key = 0;
        String sql = "INSERT INTO ingredient_reference (ingredient_id, taco, taco_key) VALUES (?, ?, ?)";

        for(Ingredient ingredient : ingredients) {
            jdbcOperations.update(sql, ingredient.getId(), tacoId, key++);
        }

    }
}
