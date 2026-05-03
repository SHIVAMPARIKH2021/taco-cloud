package sia.taco_cloud.tacos.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class Taco {

    private Long id;

    private Date createdAt = new Date();

    @NotNull
    @Size(min = 5, message = "Must be at least 5 character long")
    private String name;

    @NotNull
    @Size(min = 1, message = "Choose at least 1 ingredient")
    private List<IngredientReference> ingredients;
}
