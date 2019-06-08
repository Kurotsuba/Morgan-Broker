package group.eis.morganborker.service.Impl;

import com.google.gson.Gson;
import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.TradeHistory;
import group.eis.morganborker.repository.OrderRepository;
import group.eis.morganborker.repository.TradeHistoryRepository;
import group.eis.morganborker.service.TradeService;
import group.eis.morganborker.utils.OrderQueueUtil;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service("TradeService")
public class TradeServiceImpl implements TradeService {
    private Gson gson;

    private final OrderQueueUtil orderQueueUtil;

    private final TradeHistoryRepository tradeHistoryRepository;

    private final OrderRepository orderRepository;

    public TradeServiceImpl(OrderQueueUtil orderQueueUtil, TradeHistoryRepository tradeHistoryRepository, OrderRepository orderRepository) {
        this.orderQueueUtil = orderQueueUtil;
        this.tradeHistoryRepository = tradeHistoryRepository;
        this.orderRepository = orderRepository;
        this.gson = new Gson();
    }

    @Override
    public Integer deal(Order newOrder, Order savedOrder) {
        System.out.println("Deal between " +newOrder.getOrderID() + " " + savedOrder.getOrderID());
        if(orderQueueUtil.findCancelOrder(savedOrder.getOrderID())){
            orderQueueUtil.deleteCancelOrder(savedOrder.getOrderID());
            orderQueueUtil.popOrder(savedOrder.getFutureID(),savedOrder.getSide(), savedOrder.getPrice());
            System.out.println("Deal done with cancel");
            return 0;
        }
        Order bo = new Order();
        Order so = new Order();
        if(newOrder.getSide() == 'b'){
            bo = newOrder;
            so = savedOrder;
        }else {
            bo = savedOrder;
            so = newOrder;
        }

        Integer result = 0;
        if(newOrder.getAmount() >= savedOrder.getAmount()){
            result = savedOrder.getAmount();
            orderQueueUtil.popOrder(savedOrder.getFutureID(),savedOrder.getSide(), savedOrder.getPrice());
            orderQueueUtil.decreseAmount(savedOrder);
            System.out.println("Deal done with rest");
        }else{
            result = newOrder.getAmount();
            savedOrder.setAmount(result);
            orderQueueUtil.decreseAmount(savedOrder);
            System.out.println("Deal done without rest");
        }

        checkStopOrder(savedOrder.getFutureID(), savedOrder.getSide(), savedOrder.getPrice());
        TradeHistory th = new TradeHistory(bo.getTraderOrderID(),bo.getTraderID(),so.getTraderOrderID(),so.getTraderID(),result,savedOrder.getPrice());
        tradeHistoryRepository.save(th);

        HashMap<String, String> wsPacket = new HashMap<>();
        wsPacket.put("trader_id", newOrder.getTraderID().toString());
        wsPacket.put("order_id", newOrder.getTraderOrderID().toString());
        wsPacket.put("price", newOrder.getPrice().toString());
        wsPacket.put("amount", result.toString());
        wsPacket.put("time", th.getTimeStamp().toString());
        try {
            WebSocketServer.sendInfo(gson.toJson(wsPacket));
        } catch (IOException e) {
            e.printStackTrace();
        }

        wsPacket.replace("trader_id", savedOrder.getTraderID().toString());
        wsPacket.replace("order_id", savedOrder.getTraderOrderID().toString());
        try {
            WebSocketServer.sendInfo(gson.toJson(wsPacket));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    private void checkStopOrder(Long futureID, char side, Integer price){
        Integer stopPrice = new Integer(0);
        List<Long> stopOrderList = null;
        if (side == 'b') {
            stopPrice = orderQueueUtil.getHighestStopPrice(futureID);
            if(stopPrice == -1){
                return;
            }
            if(price <= stopPrice){
                stopOrderList = orderQueueUtil.getStopOrderList(futureID, side, stopPrice);
            }
        }else{
            stopPrice = orderQueueUtil.getLowestStopPrice(futureID);
            if(stopPrice == -1){
                return;
            }
            if(price >= stopPrice){
                stopOrderList = orderQueueUtil.getStopOrderList(futureID, side, stopPrice);
            }
        }
        if(stopOrderList == null){
            return;
        }
        System.out.println("stop order triggerred");
        for(Long orderID : stopOrderList) {
            System.out.println(orderID);
            Order addOrder = orderRepository.getOne(orderID);
            addOrder.setPrice(addOrder.getPrice2());
            orderQueueUtil.addAmount(addOrder);
            orderQueueUtil.addOrder(addOrder);
        }
    }
}
