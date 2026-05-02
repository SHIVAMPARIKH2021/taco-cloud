package sia.taco_cloud.tacos.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import sia.taco_cloud.tacos.constants.Ingredient;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Table(name = "taco")
public class Taco {

    @Id
    private Long id;

    private Date createdAt = new Date();

    @NotNull
    @Size(min = 5, message = "Must be at least 5 character long")
    private String name;

    @NotNull
    @Size(min = 1, message = "Choose at least 1 ingredient")
    @MappedCollection(idColumn = "taco_id")
    private Set<IngredientReference> ingredients;
}
