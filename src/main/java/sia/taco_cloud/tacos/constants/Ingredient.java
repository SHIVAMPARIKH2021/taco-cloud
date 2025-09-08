package sia.taco_cloud.tacos.constants;

import lombok.Data;


@Data
public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public enum Type {
        WRAP("Wrap"), PROTEIN("Protein"), VEGGIES("Veggies"), CHEESE("Cheese"), SAUCE("Sauce");
        Type(String type) {
        }
    }
}
