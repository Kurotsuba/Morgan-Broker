package group.eis.morganborker.service.Impl;

import com.google.gson.Gson;
import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.Trader;
import group.eis.morganborker.entity.TraderOrder;
import group.eis.morganborker.repository.OrderRepository;
import group.eis.morganborker.repository.TraderRepository;
import group.eis.morganborker.service.OrderService;
import group.eis.morganborker.service.TradeService;
import group.eis.morganborker.utils.OrderQueueUtil;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("OrderService")
@Repository
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderQueueUtil orderQueueUtil;

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private TradeService tradeService;

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

            System.out.println("order saved");
            synchronized (this){

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
                        testOrder(order);
                    }
                }

                HashMap<String, String> wsResult = new HashMap<>();
                wsResult.put("traderID", order.getTraderID().toString());
                wsResult.put("orderID", order.getOrderID().toString());
                wsResult.put("message", "order process done");

                Gson gson = new Gson();
                WebSocketServer.sendInfo(gson.toJson(wsResult));
            }
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

    private void marketOrder(Order order){
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
            firstOrder = orderQueueUtil.getOrder(order.getSide(), price);

            Integer dealAmount = tradeService.deal(order, firstOrder);
            if(dealAmount == rest){
                break;
            }
            rest -= dealAmount;
        }
    }


    private void fixOrder(Order order){
        char side = order.getSide();
        Integer price = 0;
        Integer rest = order.getAmount();
        if (side == 'b') {

            if(orderQueueUtil.getHighestPrice(order) >= order.getPrice()){
                orderQueueUtil.addAmount(order);
                return;
            }
            price = orderQueueUtil.getLowestPrice(order);
            while(price <= order.getPrice()){
                Integer dealAmount = tradeService.deal(orderQueueUtil.getOrder('b', price), order);
                if(dealAmount == rest){
                    return;
                }
                rest -= dealAmount;
                if(orderQueueUtil.getAmount(order.getFutureID(), 'b', price) == 0){
                    price = orderQueueUtil.getLowestPrice(order);
                }
            }
            orderQueueUtil.addAmount(order);

        }else if(side == 's'){
            if(orderQueueUtil.getLowestPrice(order) <= order.getPrice()){
                orderQueueUtil.addAmount(order);
                return;
            }
            price = orderQueueUtil.getHighestPrice(order);
            while(price >= order.getPrice()){
                Integer dealAmount = tradeService.deal(orderQueueUtil.getOrder('s', price), order);
                if(dealAmount == rest){
                    return;
                }
                rest -= dealAmount;
                if(orderQueueUtil.getAmount(order.getFutureID(), 's', price) == 0){
                    price = orderQueueUtil.getHighestPrice(order);
                }
            }
            orderQueueUtil.addAmount(order);
        }


    }

    private void stopOrder(Order order){};

    private void cancelOrder(Order order){};

    private void testOrder(Order order){
        orderQueueUtil.addAmount(order);
    }

}
