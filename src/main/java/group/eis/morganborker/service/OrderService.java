package group.eis.morganborker.service;

import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.TraderOrder;

public interface OrderService {
    Long recieveOrder(TraderOrder traderOrder);
    Order findOrder(Long id);
    Order findOrder(Long traderID, Long traderOrderID);
}
