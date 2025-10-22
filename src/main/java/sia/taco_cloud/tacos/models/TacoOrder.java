package sia.taco_cloud.tacos.models;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.util.ArrayList;
import java.util.List;

@Data
public class TacoOrder {

    @NotBlank(message = "Delivery name is required field")
    private String deliveryName;

    @NotBlank(message = "Delivery street is required field")
    private String deliveryStreet;

    @NotBlank(message = "Delivery city is required field")
    private String deliveryCity;

    @NotBlank(message = "Delivery state is required field")
    private String deliveryState;

    @NotBlank(message = "Delivery zip code is required field")
    private String deliveryZip;

    @CreditCardNumber(message = "Invalid credit card number")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([/])([2-9][0-9]])$", message = "Valid format is MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid cvv")
    private String ccCvv;

    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }
}
