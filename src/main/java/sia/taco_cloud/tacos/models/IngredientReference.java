package sia.taco_cloud.tacos.models;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("ingredient_reference")
public class IngredientReference {

    private final String ingredientId;
}
