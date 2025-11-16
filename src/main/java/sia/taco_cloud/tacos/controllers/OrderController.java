package sia.taco_cloud.tacos.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import sia.taco_cloud.tacos.models.TacoOrder;
import sia.taco_cloud.tacos.repositories.OrderRepository;

@Slf4j
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
@Controller
public class OrderController {

    OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order, Errors errors,
                               SessionStatus sessionStatus) {

        if(errors.hasErrors()) {
            return "orderForm";
        }

        orderRepository.save(order);
        log.info("Order submitted: {}", order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
