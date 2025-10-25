package sia.taco_cloud.tacos.repositories.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import sia.taco_cloud.tacos.constants.Ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class IngredientRepositoryImplementationTest {

    private JdbcTemplate jdbcTemplate;
    private IngredientRepositoryImplementation repository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new IngredientRepositoryImplementation(jdbcTemplate);
    }

    @Test
    void findAll_returnsAllIngredients() {
        Ingredient wrap = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
        Ingredient pto = new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);

        when(jdbcTemplate.query(anyString(), any(org.springframework.jdbc.core.RowMapper.class)))
                .thenReturn(Arrays.asList(wrap, pto));

        Iterable<Ingredient> result = repository.findAll();

        assertThat(result).isInstanceOf(List.class);
        List<Ingredient> list = (List<Ingredient>) result;
        assertThat(list).hasSize(2).containsExactly(wrap, pto);
    }

    @Test
    void findById_returnsIngredientWhenPresent() {
        Ingredient wrap = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(org.springframework.jdbc.core.RowMapper.class)))
                .thenReturn(Arrays.asList(wrap));

        Optional<Ingredient> found = repository.findById("FLTO");

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(wrap);

        ArgumentCaptor<Object[]> captor = ArgumentCaptor.forClass(Object[].class);
        verify(jdbcTemplate).query(anyString(), captor.capture(), any(org.springframework.jdbc.core.RowMapper.class));
        Object[] params = captor.getValue();
        assertThat(params).containsExactly("FLTO");
    }

    @Test
    void findById_returnsEmptyWhenNotPresent() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(org.springframework.jdbc.core.RowMapper.class)))
                .thenReturn(Arrays.asList());

        Optional<Ingredient> found = repository.findById("XXXX");

        assertThat(found).isEmpty();
    }

    @Test
    void save_insertsIngredient() {
        Ingredient cheese = new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE);

        when(jdbcTemplate.update(anyString(), any(), any(), any())).thenReturn(1);

        Ingredient saved = repository.save(cheese);

        assertThat(saved).isSameAs(cheese);

        verify(jdbcTemplate).update(anyString(), eq(cheese.getId()), eq(cheese.getName()), eq(cheese.getType().name()));
    }

}
