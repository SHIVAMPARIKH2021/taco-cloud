package sia.taco_cloud.tacos.repositories.implementations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sia.taco_cloud.tacos.constants.Ingredient;
import sia.taco_cloud.tacos.repositories.IngredientRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepositoryImplementation implements IngredientRepository {

    private final JdbcTemplate jdbcTemplate;

    public IngredientRepositoryImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Find all the available ingredients
     * @return iterable of an Ingredient objects
     */
    @Override
    public Iterable<Ingredient> findAll() {
        return jdbcTemplate.query("select id, name, type from ingredients;",
                this::mapRow
        );
    }

    /**
     * Find the Ingredient by id
     * @param id an unique id of an Ingredient
     * @return Ingredient object
     */
    @Override
    public Optional<Ingredient> findById(String id) {
        List<Ingredient> ingredients;
        ingredients = jdbcTemplate.query("select id, name, type from ingredients where id = ?;",
                new Object[]{id},
                this::mapRow
        );
        return ingredients.isEmpty()
                ? Optional.empty()
                : Optional.ofNullable(ingredients.get(0));
    }

    /**
     * Save the Ingredient in the data base
     * @param ingredient an Ingredient object
     * @return an Ingredient object to be saved
     */
    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update("insert into ingredients (id, name, type) values (?, ?, ?)",
                ingredient.getId(), ingredient.getName(), ingredient.getType().name());
        return ingredient;
    }

    /**
     * A method to map the result set to Ingredient object
     * @param resultSet Result set of a query
     * @param rowNum row number of a table
     * @return Ingredient object
     * @throws SQLException SQL exception
     */
    private Ingredient mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Ingredient(
                resultSet.getString("id"),
                resultSet.getString("name"),
                Ingredient.Type.valueOf(resultSet.getString("type"))
        );

    }
}
