package sia.taco_cloud.tacos.repositories.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sia.taco_cloud.tacos.constants.Ingredient;
import sia.taco_cloud.tacos.repositories.IngredientRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class IngredientRepositoryImplementation implements IngredientRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private String ingredientFindAll;

    @Autowired
    private String ingredientFindById;

    @Autowired
    private String ingredientInsert;

    public IngredientRepositoryImplementation(JdbcTemplate jdbcTemplate,
                                              String ingredientFindAll,
                                              String ingredientFindById,
                                              String ingredientInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.ingredientFindAll = ingredientFindAll;
        this.ingredientFindById = ingredientFindById;
        this.ingredientInsert = ingredientInsert;
    }

    /**
     * Find all the available ingredients
     * @return iterable of an Ingredient objects
     */
    @Override
    public Iterable<Ingredient> findAll() {
        return jdbcTemplate.query(ingredientFindAll,
                this::mapRow
        );
    }

    /**
     * Find the Ingredient by id
     * @param id a unique id of an Ingredient
     * @return Ingredient object
     */
    @Override
    public Optional<Ingredient> findById(String id) {
        try {
            Ingredient ingredient = jdbcTemplate.queryForObject(
                    ingredientFindById,
                    this::mapRow,
                    id
            );
            return Optional.ofNullable(ingredient);
        }
        catch(org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Save the Ingredient in the database
     * @param ingredient an Ingredient object
     * @return an Ingredient object to be saved
     */
    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update(ingredientInsert,
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
