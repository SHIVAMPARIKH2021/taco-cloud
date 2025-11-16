package sia.taco_cloud.tacos.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;
import sia.taco_cloud.tacos.constants.Ingredient;
import sia.taco_cloud.tacos.models.Taco;
import sia.taco_cloud.tacos.models.TacoOrder;
import sia.taco_cloud.tacos.repositories.implementations.IngredientRepositoryImplementation;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    private IngredientRepositoryImplementation ingredientRepositoryImplementation;

    @Autowired
    public DesignTacoController(IngredientRepositoryImplementation ingredientRepositoryImplementation) {
        this.ingredientRepositoryImplementation = ingredientRepositoryImplementation;
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepositoryImplementation.findAll();
        Ingredient.Type[] types = Ingredient.Type.values();
        for(Ingredient.Type type :types) {
            model.addAttribute(type.toString().toLowerCase(), filterByType((List<Ingredient>) ingredients, type));
        }
    }

    @GetMapping
    public String showDesignFormat() {
        return "design";
    }

    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors,
                              @ModelAttribute TacoOrder tacoOrder) {

        if(errors.hasErrors()) {
            log.error("Errors found in taco design: {}", errors);
            return "design";
        }

        tacoOrder.addTaco(taco);
        log.info("Processing taco :{}", taco);
        return "redirect:/orders/current";
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getType().toString()
                        .equalsIgnoreCase(type.toString()))
                .collect(Collectors.toList());
    }
}
