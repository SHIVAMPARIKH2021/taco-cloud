package sia.taco_cloud.tacos.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("ingredients") // Explicitly map to the plural table name
public class Ingredient {
    @Id
    private String id;
    private String name;
    private String type;

    // ... rest of the class
}
