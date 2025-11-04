package sia.taco_cloud.tacos.repositories;

import sia.taco_cloud.tacos.models.TacoOrder;

public interface OrderRepository {

    TacoOrder save(TacoOrder order);
}
