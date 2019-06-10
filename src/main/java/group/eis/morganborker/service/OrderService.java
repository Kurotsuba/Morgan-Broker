package group.eis.morganborker.service;

import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.TraderOrder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderService {
    Long receiveOrder(TraderOrder traderOrder);
    Order findOrder(Long id);
    Order findOrder(Long traderID, Long traderOrderID);
    List<Order> findOrderByFuture(String futureName, String period);
    Map<String, Map<String, Integer>> findActiveOrderByFuture(String name, String period);
}
