package group.eis.morganborker.service.Impl;

import com.google.gson.Gson;
import group.eis.morganborker.controller.ActiveOrderPOJO;
import group.eis.morganborker.entity.Future;
import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.Trader;
import group.eis.morganborker.entity.TraderOrder;
import group.eis.morganborker.repository.FutureRepository;
import group.eis.morganborker.repository.OrderRepository;
import group.eis.morganborker.repository.TraderRepository;
import group.eis.morganborker.service.OrderService;
import group.eis.morganborker.service.TradeService;
import group.eis.morganborker.utils.OrderQueueUtil;
import group.eis.morganborker.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private FutureRepository futureRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Long receiveOrder(TraderOrder traderOrder) {
        try{
            Order order = new Order(traderOrder);

            // skip repeated order
            if(orderRepository.findOrderByTraderIDAndTraderOrderID(order.getTraderID(), order.getTraderOrderID()) != null){
                System.out.println("order skipped");
                return 0l;
            }
//            if(traderRepository.getOne(order.getTraderID()) == null){
//                Trader trader = new Trader(order.getTraderID(), order.getTraderName());
//                traderRepository.save(trader);
//            }

            orderRepository.save(order);

            System.out.println("order saved");
            synchronized (this) {

                switch (order.getType()) {
                    case 'm': {
                        marketOrder(order);
                        break;
                    }
                    case 'l': {
                        try {
                            orderQueueUtil.addOrder(order);
                            limitOrder(order);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 's': {
                        stopOrder(order);
                        break;
                    }
                    case 'c': {
                        cancelOrder(order);
                        break;
                    }
                    default: {
                        testOrder(order);
                        break;
                    }
                }
            }
                HashMap<String, String> wsResult = new HashMap<>();
                wsResult.put("traderID", order.getTraderID().toString());
                wsResult.put("traderName", traderRepository.getOne(order.getTraderID()).getTraderName());
                wsResult.put("orderID", order.getOrderID().toString());
                wsResult.put("type", String.valueOf(order.getType()));
                wsResult.put("amount", order.getAmount().toString());
                wsResult.put("future_id", order.getFutureID().toString());
                wsResult.put("price", order.getPrice().toString());
                wsResult.put("message", "order process done");

                Gson gson = new Gson();
                String jsonStr = gson.toJson(wsResult);
                jsonStr = jsonStr.substring(0, jsonStr.lastIndexOf('}')) + ",\"msg_type\":\"order_process_message\"}";
                try{
                    WebSocketServer.sendInfo(jsonStr);
                }catch (Exception e){
                    e.printStackTrace();
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

    @Override
    public List<Order> findOrderByFuture(String futureName, String period) {
        Long futureID = futureRepository.findByFutureNameAndPeriod(futureName, period).getFutureID();
        return orderRepository.findAllByFutureID(futureID);
    }

    @Override
    public List<ActiveOrderPOJO> findActiveOrderByFuture(String name, String period) {
        Future future = futureRepository.findByFutureNameAndPeriod(name, period);
        Set<Object> buyPriceSet = redisUtil.hashKeys(future.getFutureID()+"_buy_list");
        Set<Object> sellPriceSet = redisUtil.hashKeys(future.getFutureID()+"_sell_list");
        Set<Integer> IDSet = new HashSet<>();

        for(Object price : buyPriceSet){
            List<Object> idList = redisUtil.listGet(future.getFutureID()+"_b_"+(String) price, 0, -1);
            for(Object id : idList){
                IDSet.add((Integer)id);
            }
        }

        for(Object price : sellPriceSet){
            List<Object> idList = redisUtil.listGet(future.getFutureID()+"_s_"+(String) price, 0, -1);
            for(Object id : idList){
                IDSet.add((Integer)id);
            }
        }
        List<ActiveOrderPOJO> result = new LinkedList<>();
        for(Integer id : IDSet){
            ActiveOrderPOJO pojo = new ActiveOrderPOJO();
            Order order = orderRepository.getOne(id.longValue());
            pojo.futureID = order.getFutureID();
            pojo.oldID = order.getTraderOrderID();
            pojo.orderID = order.getOrderID();
            pojo.price = order.getPrice();
            pojo.qty = order.getAmount();
            pojo.side = order.getSide();
            pojo.type = order.getType();
            pojo.traderID = order.getTraderID();
            pojo.rest = (Integer)redisUtil.get(id.toString());
            result.add(pojo);
        }

        return result;

    }


    private void marketOrder(Order order){
        Integer rest = order.getAmount();
        Order firstOrder = new Order();
        Integer price = 0;
        char oppSide = 'b';
        while(rest > 0){
            if(order.getSide() == 'b'){
                price = orderQueueUtil.getLowestPrice(order);
                oppSide = 's';
            }else if(order.getSide() == 's'){
                price = orderQueueUtil.getHighestPrice(order);
                oppSide = 'b';
            }
            if(price == -1){
                break;
            }

            System.out.println("deal start "+price+" "+oppSide);
            System.out.println("deal order id " + orderQueueUtil.getOrder(order.getFutureID(),oppSide, price));
            Long orderID = orderQueueUtil.getOrder(order.getFutureID(),oppSide, price);
            if(orderID < 0){
                break;
            }
            firstOrder = orderRepository.getOne(orderID);

            Integer dealAmount = tradeService.deal(order, firstOrder);
            if(dealAmount.equals(rest)){
                break;
            }
            rest -= dealAmount;
            order.setAmount(rest);
        }
    }


    private void limitOrder(Order order){
        char side = order.getSide();
        Integer price = 0;
        Integer rest = order.getAmount();
        if (side == 'b') {

            if(orderQueueUtil.getHighestPrice(order) >= order.getPrice()){
                orderQueueUtil.addAmount(order);
                return;
            }
            price = orderQueueUtil.getLowestPrice(order);
            if(price == -1){
                orderQueueUtil.addAmount(order);
                return;
            }
            while(price <= order.getPrice()){
                Long orderID = orderQueueUtil.getOrder(order.getFutureID(),'s', price);
                if(orderID < 0){
                    return;
                }
                Order savedOrder = orderRepository.getOne(orderID);
                Integer dealAmount = tradeService.deal(order, savedOrder);
                if(dealAmount.equals(rest)){
                    return;
                }
                rest -= dealAmount;
                order.setAmount(rest);
                if(orderQueueUtil.getAmount(order.getFutureID(), 'b', price) == 0){
                    price = orderQueueUtil.getLowestPrice(order);
                }
            }
            orderQueueUtil.addAmount(order);

        }else if(side == 's'){
            Integer lPrice = orderQueueUtil.getLowestPrice(order);
            if( lPrice!= -1 && lPrice <= order.getPrice()){
                orderQueueUtil.addAmount(order);
                return;
            }
            price = orderQueueUtil.getHighestPrice(order);
            if(price == -1){
                orderQueueUtil.addAmount(order);
                return;
            }
            while(price >= order.getPrice()){
                Long orderID = orderQueueUtil.getOrder(order.getFutureID(),'b', price);
                if(orderID < 0){
                    return;
                }
                Order savedOrder = orderRepository.getOne(orderID);
                Integer dealAmount = tradeService.deal(order, savedOrder);
                if(dealAmount.equals(rest)){
                    return;
                }
                rest -= dealAmount;
                order.setAmount(rest);
                if(orderQueueUtil.getAmount(order.getFutureID(), 's', price) == 0){
                    price = orderQueueUtil.getHighestPrice(order);
                }
            }
            orderQueueUtil.addAmount(order);
        }


    }

    private void stopOrder(Order order){
        orderQueueUtil.addStopOrder(order);
    }

    private void cancelOrder(Order order){
        Order target = orderRepository.getOne(order.getCancelID());
        System.out.println(target.getOrderID());
        System.out.println(orderQueueUtil.findCancelOrder(target.getOrderID()));
        if(target != null && !orderQueueUtil.findCancelOrder(target.getOrderID())) {
            if(target.getType() != 'c') {
                if(target.getType() != 's'){
                    target.setAmount(orderQueueUtil.getActiveOrderRest(target));
                    orderQueueUtil.decreseAmount(target);
                }
                orderQueueUtil.addCancelOrder(target);
                HashMap<String, Long> wsPack = new HashMap<>();
                wsPack.put("order_id", order.getOrderID());
                wsPack.put("cancel_id", order.getCancelID());
                wsPack.put("trader_id", order.getTraderID());
                Gson gson = new Gson();
                String jsonStr = gson.toJson(wsPack);
                jsonStr = jsonStr.substring(0, jsonStr.lastIndexOf('}')) + ",\"msg_type\":\"cancel_message\"}";
                System.out.println("Order canceled");
                try{
                    WebSocketServer.sendInfo(jsonStr);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void testOrder(Order order){
        orderQueueUtil.addAmount(order);
    }

}
