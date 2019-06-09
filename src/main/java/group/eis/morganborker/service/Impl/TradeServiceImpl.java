package group.eis.morganborker.service.Impl;

import com.google.gson.Gson;
import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.TradeHistory;
import group.eis.morganborker.repository.OrderRepository;
import group.eis.morganborker.repository.TradeHistoryRepository;
import group.eis.morganborker.service.MarketService;
import group.eis.morganborker.service.TradeService;
import group.eis.morganborker.utils.OrderQueueUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("TradeService")
public class TradeServiceImpl implements TradeService {
    private Gson gson;

    private final OrderQueueUtil orderQueueUtil;

    private final TradeHistoryRepository tradeHistoryRepository;

    private final OrderRepository orderRepository;

    private final MarketService marketService;

    private static int dealtime;

    private final int pushFrequency;

    public TradeServiceImpl(OrderQueueUtil orderQueueUtil, TradeHistoryRepository tradeHistoryRepository, OrderRepository orderRepository, MarketService marketService) {
        this.orderQueueUtil = orderQueueUtil;
        this.marketService = marketService;
        this.tradeHistoryRepository = tradeHistoryRepository;
        this.orderRepository = orderRepository;
        this.gson = new Gson();
        this.dealtime = 0;
        this.pushFrequency = 5;
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

        if(dealtime % pushFrequency == 0){
            Map<String, HashMap<String, Integer>> result = marketService.getMarket(savedOrder.getFutureID());
            String jsonStr = gson.toJson(result);
            jsonStr = jsonStr.substring(0, jsonStr.lastIndexOf('}')) + ",\"type\":\"market_depth\"}";
            try {
                WebSocketServer.sendInfo(jsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dealtime++;

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
        TradeHistory th = new TradeHistory(bo.getTraderOrderID(),bo.getTraderID(),so.getTraderOrderID(),so.getTraderID(),result,savedOrder.getPrice(), savedOrder.getSide(), savedOrder.getFutureID());
        tradeHistoryRepository.save(th);

        HashMap<String, String> wsPacket = new HashMap<>();
        wsPacket.put("trader_id", newOrder.getTraderID().toString());
        wsPacket.put("order_id", newOrder.getTraderOrderID().toString());
        wsPacket.put("price", savedOrder.getPrice().toString());
        wsPacket.put("amount", result.toString());
        wsPacket.put("time", th.getTimeStamp().toString());
        String jsonStr = gson.toJson(wsPacket);
        jsonStr = jsonStr.substring(0, jsonStr.lastIndexOf('}')) + ",\"type\":\"deal_message\"}";
        try {
            WebSocketServer.sendInfo(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonStr = gson.toJson(wsPacket);
        jsonStr = jsonStr.substring(0, jsonStr.lastIndexOf('}')) + ",\"type\":\"deal_message\"}";
        wsPacket.replace("trader_id", savedOrder.getTraderID().toString());
        wsPacket.replace("order_id", savedOrder.getTraderOrderID().toString());
        try {
            WebSocketServer.sendInfo(jsonStr);
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
        for(Long orderID : stopOrderList) {
            System.out.println(orderID);
            Order addOrder = orderRepository.getOne(orderID);
            addOrder.setPrice(addOrder.getPrice2());
            orderQueueUtil.addAmount(addOrder);
            orderQueueUtil.addOrder(addOrder);
        }
    }
}
