package sia.taco_cloud.tacos.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import sia.taco_cloud.tacos.constants.Ingredient;
import sia.taco_cloud.tacos.repositories.implementations.IngredientRepositoryImplementation;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class IngredientByIdConverterTest {

    private IngredientRepositoryImplementation repo;
    private Converter<String, Ingredient> converter;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(IngredientRepositoryImplementation.class);
        converter = new IngredientByIdConverter(repo);
    }

    @Test
    void convert_returnsIngredientWhenFound() {
        Ingredient expected = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
        when(repo.findById(eq("FLTO"))).thenReturn(Optional.of(expected));

        Ingredient actual = converter.convert("FLTO");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void convert_returnsNullWhenNotFound() {
        when(repo.findById(eq("XXXX"))).thenReturn(Optional.empty());

        Ingredient actual = converter.convert("XXXX");

        assertThat(actual).isNull();
    }
}

