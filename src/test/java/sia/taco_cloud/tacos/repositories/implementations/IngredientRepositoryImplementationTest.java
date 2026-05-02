package sia.taco_cloud.tacos.repositories.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sia.taco_cloud.tacos.config.QueryConfig;
import sia.taco_cloud.tacos.config.QueryConfigTest;
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

    @Autowired
    private String ingredientFindAllTest;

    @Autowired
    private String ingredientFindByIdTest;

    @Autowired
    private String ingredientInsertTest;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new IngredientRepositoryImplementation(
                jdbcTemplate,
                ingredientFindAllTest,
                ingredientFindByIdTest,
                ingredientInsertTest
        );
    }

    @Test
    void findAll_returnsAllIngredients() {
        Ingredient wrap = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
        Ingredient pto = new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);

        when(jdbcTemplate.query(eq(ingredientFindAllTest), any(RowMapper.class)))
                .thenReturn(Arrays.asList(wrap, pto));

        Iterable<Ingredient> result = repository.findAll();

        assertThat(result).isInstanceOf(List.class);
        List<Ingredient> list = (List<Ingredient>) result;
        assertThat(list).hasSize(2).containsExactly(wrap, pto);
    }

    @Test
    void findById_returnsIngredientWhenPresent() {
        Ingredient wrap = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);

        when(jdbcTemplate.queryForObject(eq(ingredientFindByIdTest), any(RowMapper.class), eq("FLTO")))
                .thenReturn(wrap);

        Optional<Ingredient> found = repository.findById("FLTO");

        assertThat(found).isPresent();
        assertThat(found).isEqualTo(Optional.of(wrap));
    }

    @Test
    void findById_returnsEmptyWhenNotPresent() {
        when(jdbcTemplate.query(eq(ingredientFindByIdTest), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Collections.emptyList());

        Optional<Ingredient> found = repository.findById("XXXX");

        assertThat(found).isEmpty();
    }

    @Test
    void save_insertsIngredient() {
        Ingredient cheese = new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE);

        when(jdbcTemplate.update(eq(ingredientInsertTest), any(), any(), any())).thenReturn(1);

        Ingredient saved = repository.save(cheese);

        assertThat(saved).isSameAs(cheese);

        verify(jdbcTemplate).update(eq(ingredientInsertTest), eq(cheese.getId()), eq(cheese.getName()), eq(cheese.getType().name()));
    }

}