package group.eis.morganborker.service.Impl;

import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.Trader;
import group.eis.morganborker.entity.TraderOrder;
import group.eis.morganborker.repository.OrderRepository;
import group.eis.morganborker.repository.TraderRepository;
import group.eis.morganborker.service.OrderService;
import group.eis.morganborker.utils.OrderQueueUtil;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service("OrderService")
@Repository
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderQueueUtil orderQueueUtil;

    @Autowired
    private TraderRepository traderRepository;


    @Override
    public Long receiveOrder(TraderOrder traderOrder) {
        try{
            Order order = new Order(traderOrder);

            // skip repeated order
            if(orderRepository.findOrderByTraderIDAndTraderOrderID(order.getTraderID(), order.getTraderOrderID()) != null){
                System.out.println("order skipped");
                return 0l;
            }
            if(!traderRepository.findById(order.getTraderID()).isPresent()){
                Trader trader = new Trader(order.getTraderID(), order.getTraderName());
                traderRepository.save(trader);
            }

            // record time queue of order
            orderQueueUtil.addOrder(order);
            orderRepository.save(order);

            switch (order.getType()){
                case 'm':{
                    marketOrder(order);
                }case 'f':{
                    fixOrder(order);
                }case 's':{
                    stopOrder(order);
                }case 'c':{
                    cancelOrder(order);
                }default:{
                    break;
                }
            }
            System.out.println("order saved");
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

    private Long marketOrder(Order order){
        Integer rest = order.getAmount();
        Order firstOrder = new Order();
        Integer price = 0;
        while(rest > 0){
            if(order.getSide() == 'b'){
                price = orderQueueUtil.getLowestPrice(order);
            }else if(order.getSide() == 's'){
                price = orderQueueUtil.getHighestPrice(order);
            }
            if(price == -1){
                break;
            }
            firstOrder = orderQueueUtil.getOrder(price);

            if(rest >= firstOrder.getAmount()){
                rest -= firstOrder.getAmount();
                orderQueueUtil.popOrder(price);
            }else{
                orderQueueUtil.headOrderUpdate(price, firstOrder.getAmount()-rest);
                rest = 0;
            }
        }
        return 0l;
    };
    private Long fixOrder(Order order){return 0l;};
    private Long stopOrder(Order order){return 0l;};
    private Long cancelOrder(Order order){return 0l;};

}
