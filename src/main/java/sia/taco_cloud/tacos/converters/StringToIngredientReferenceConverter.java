package sia.taco_cloud.tacos.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sia.taco_cloud.tacos.models.IngredientReference;

@Component
public class StringToIngredientReferenceConverter implements Converter<String, IngredientReference> {

    @Override
    public IngredientReference convert(String source) {
        // This converts the String (e.g. "COTO") into your link object
        return new IngredientReference(source);
    }
}