package group.eis.morganborker.service.Impl;

import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.TraderOrder;
import group.eis.morganborker.repository.OrderRepository;
import group.eis.morganborker.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Long recieveOrder(TraderOrder traderOrder) {
        try{
            Order order = new Order(traderOrder);
            orderRepository.save(order);
            return order.getOrderID();
        }catch (Exception e){
            e.printStackTrace();
            return 0l;
        }
    }

    @Override
    public Order findOrder(Long id) {
        return orderRepository.getOne(id);
    }

    @Override
    public Order findOrder(Long traderID, Long traderOrderID) {
        return orderRepository.findOrderByTraderIDAndTraderOrderID(traderID, traderOrderID);
    }


}
