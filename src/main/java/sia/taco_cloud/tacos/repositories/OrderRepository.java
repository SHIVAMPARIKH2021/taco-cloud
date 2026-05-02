package sia.taco_cloud.tacos.repositories;

import org.springframework.data.repository.CrudRepository;
import sia.taco_cloud.tacos.models.TacoOrder;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {

    TacoOrder save(TacoOrder order);
}
