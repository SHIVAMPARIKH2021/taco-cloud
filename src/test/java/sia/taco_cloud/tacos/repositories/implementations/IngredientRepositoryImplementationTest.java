package sia.taco_cloud.tacos.repositories.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import sia.taco_cloud.tacos.constants.Ingredient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngredientRepositoryImplementationTest {

    private JdbcTemplate jdbcTemplate;
    private IngredientRepositoryImplementation repository;
    private String ingredientFindAll;
    private String ingredientFindById;
    private String ingredientInsert;

    @BeforeEach
    void setUp() {
        ingredientFindAll = "select id, name, type from ingredients";
        ingredientFindById = "select id, name, type from ingredients where id = ?";
        ingredientInsert = "insert into ingredients (id, name, type) values (?, ?, ?)";

        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new IngredientRepositoryImplementation(jdbcTemplate, ingredientFindAll, ingredientFindById, ingredientInsert);
    }

    @Test
    void findAll_returnsAllIngredients() {
        Ingredient wrap = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
        Ingredient pto = new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);

        when(jdbcTemplate.query(eq(ingredientFindAll), any(RowMapper.class)))
                .thenReturn(Arrays.asList(wrap, pto));

        Iterable<Ingredient> result = repository.findAll();

        assertThat(result).isInstanceOf(List.class);
        List<Ingredient> list = (List<Ingredient>) result;
        assertThat(list).hasSize(2).containsExactly(wrap, pto);
    }

    @Test
    void findById_returnsIngredientWhenPresent() {
        Ingredient wrap = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);

        when(jdbcTemplate.queryForList(eq(ingredientFindById), eq(Ingredient.class)))
                .thenReturn(List.of(wrap));

        Optional<Ingredient> found = repository.findById("FLTO");

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(wrap);
    }

    @Test
    void findById_returnsEmptyWhenNotPresent() {
        when(jdbcTemplate.query(eq(ingredientFindById), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Collections.emptyList());

        Optional<Ingredient> found = repository.findById("XXXX");

        assertThat(found).isEmpty();
    }

    @Test
    void save_insertsIngredient() {
        Ingredient cheese = new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE);

        when(jdbcTemplate.update(eq(ingredientInsert), any(), any(), any())).thenReturn(1);

        Ingredient saved = repository.save(cheese);

        assertThat(saved).isSameAs(cheese);

        verify(jdbcTemplate).update(eq(ingredientInsert), eq(cheese.getId()), eq(cheese.getName()), eq(cheese.getType().name()));
    }

}