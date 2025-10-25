package sia.taco_cloud.tacos.repositories;

import sia.taco_cloud.tacos.constants.Ingredient;

import java.util.Optional;

public interface IngredientRepository {

    Iterable<Ingredient> findAll();

    Optional<Ingredient> findById(String id);

    Ingredient save(Ingredient ingredient);
}
