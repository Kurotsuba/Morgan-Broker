package group.eis.morganborker.service.Impl;

import com.google.gson.Gson;
import group.eis.morganborker.entity.Future;
import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.TradeHistory;
import group.eis.morganborker.repository.FutureRepository;
import group.eis.morganborker.repository.OrderRepository;
import group.eis.morganborker.repository.TradeHistoryRepository;
import group.eis.morganborker.repository.TraderRepository;
import group.eis.morganborker.service.MarketService;
import group.eis.morganborker.service.TradeService;
import group.eis.morganborker.utils.OrderQueueUtil;
import group.eis.morganborker.utils.TradeHistoryPack;
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

    private final FutureRepository futureRepository;

    private final TraderRepository traderRepository;

    private static int dealtime;

    private final int pushFrequency;

    public TradeServiceImpl(OrderQueueUtil orderQueueUtil, TradeHistoryRepository tradeHistoryRepository, OrderRepository orderRepository, MarketService marketService, FutureRepository futureRepository, TraderRepository traderRepository) {
        this.orderQueueUtil = orderQueueUtil;
        this.marketService = marketService;
        this.tradeHistoryRepository = tradeHistoryRepository;
        this.orderRepository = orderRepository;
        this.futureRepository = futureRepository;
        this.traderRepository = traderRepository;
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
            Map<String, HashMap<String, Integer>> result = marketService.getMarket(savedOrder.getFutureID(), 3);
            String jsonStr = gson.toJson(result);
            jsonStr = jsonStr.substring(0, jsonStr.lastIndexOf('}')) + ",\"msg_type\":\"market_depth\"}";
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

        Integer savedRest = orderQueueUtil.getActiveOrderRest(savedOrder);
        Integer result = 0;
        if(newOrder.getAmount() >= savedRest){
            result = savedRest;
            orderQueueUtil.popOrder(savedOrder.getFutureID(),savedOrder.getSide(), savedOrder.getPrice());
            orderQueueUtil.decreaseActiveOrder(savedOrder, result);
            savedOrder.setAmount(savedRest);
            orderQueueUtil.decreseAmount(savedOrder);
            System.out.println("Deal done with rest");
        }else{
            result = newOrder.getAmount();
            savedOrder.setAmount(result);
            orderQueueUtil.decreaseActiveOrder(savedOrder, result);
            orderQueueUtil.decreseAmount(savedOrder);
            System.out.println("Deal done without rest");
        }

        checkStopOrder(savedOrder.getFutureID(), savedOrder.getSide(), savedOrder.getPrice());
        TradeHistory th = new TradeHistory(bo.getTraderOrderID(),bo.getTraderID(),so.getTraderOrderID(),so.getTraderID(),result,savedOrder.getPrice(), savedOrder.getSide(), savedOrder.getFutureID());
        tradeHistoryRepository.save(th);

        TradeHistoryPack pack = new TradeHistoryPack();
        Future future = futureRepository.getOne(so.getFutureID());
        pack.seller_order_id = so.getTraderOrderID();
        pack.buyer_order_id = bo.getTraderOrderID();
        pack.future_name = future.getFutureName();
        pack.period = future.getPeriod();
        pack.buyer_name = traderRepository.getOne(bo.getTraderID()).getTraderName();
        pack.seller_name = traderRepository.getOne(so.getTraderID()).getTraderName();
        pack.initiator_side = newOrder.getSide();
        pack.price = th.getPrice();
        pack.qty = th.getAmount();
        pack.tradeID = th.getTradeID();
        pack.time = th.getTimeStamp();
        String jsonStr = gson.toJson(pack);
        jsonStr = jsonStr.substring(0, jsonStr.lastIndexOf('}')) + ",\"msg_type\":\"deal_message\"}";
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
