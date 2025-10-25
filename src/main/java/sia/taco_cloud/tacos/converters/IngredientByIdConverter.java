package sia.taco_cloud.tacos.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sia.taco_cloud.tacos.constants.Ingredient;
import sia.taco_cloud.tacos.repositories.implementations.IngredientRepositoryImplementation;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    private IngredientRepositoryImplementation ingredientRepositoryImplementation;

    @Autowired
    public IngredientByIdConverter(IngredientRepositoryImplementation ingredientRepositoryImplementation) {
        this.ingredientRepositoryImplementation = ingredientRepositoryImplementation;
    }

    @Override
    public Ingredient convert(String id) {
        return ingredientRepositoryImplementation.findById(id).orElse(null);
    }
}
