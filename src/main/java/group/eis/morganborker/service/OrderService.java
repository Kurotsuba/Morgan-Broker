package group.eis.morganborker.service;

import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.TraderOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderService {
    Long receiveOrder(TraderOrder traderOrder);
    Order findOrder(Long id);
    Order findOrder(Long traderID, Long traderOrderID);
}
