package sia.taco_cloud.tacos.models;

import lombok.Data;
import sia.taco_cloud.tacos.constants.Ingredient;

import java.util.List;

@Data
public class Taco {
    private String name;
    private List<Ingredient> ingredients;
}
