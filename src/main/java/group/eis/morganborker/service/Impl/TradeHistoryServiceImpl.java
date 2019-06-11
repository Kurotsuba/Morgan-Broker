package group.eis.morganborker.service.Impl;

import group.eis.morganborker.entity.Future;
import group.eis.morganborker.entity.TradeHistory;
import group.eis.morganborker.entity.Trader;
import group.eis.morganborker.repository.FutureRepository;
import group.eis.morganborker.repository.OrderRepository;
import group.eis.morganborker.repository.TradeHistoryRepository;
import group.eis.morganborker.repository.TraderRepository;
import group.eis.morganborker.service.TradeHistoryService;
import group.eis.morganborker.utils.TradeHistoryPack;
import group.eis.morganborker.utils.TradeHistoryPack2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service("TradeHistoryService")
public class TradeHistoryServiceImpl implements TradeHistoryService {
    @Autowired
    TradeHistoryRepository tradeHistoryRepository;

    @Autowired
    TraderRepository traderRepository;

    @Autowired
    FutureRepository futureRepository;

    @Autowired
    OrderRepository orderRepository;

    @Override
    public Map<String, List<TradeHistoryPack2>> getHistory(Long traderID) {
        Map<String, List<TradeHistoryPack2>> result = new HashMap<>();
        List<TradeHistoryPack2> buyList = new LinkedList<>();
        List<TradeHistoryPack2> sellList = new LinkedList<>();
        List<TradeHistory> tmp = tradeHistoryRepository.findAllByBuyTraderID(traderID);
        for(TradeHistory h : tmp){
            Future future = futureRepository.getOne(h.getFutureID());
            TradeHistoryPack2 pack = new TradeHistoryPack2();

            pack.amount = h.getAmount();
            pack.buyTraderID = h.getBuyTraderID();
            pack.buyTraderOrderID = h.getBuyTraderOrderID();
            pack.futureID = h.getFutureID();
            pack.product = future.getFutureName();
            pack.sellTraderID = h.getSellTraderID();
            pack.sellTraderOrderID = h.getSellTraderOrderID();
            pack.timeStamp = h.getTimeStamp();
            pack.tradeID = h.getTradeID();
            pack.price = h.getPrice();
            pack.period = future.getPeriod();
            pack.initiatorSide = h.getInitiatorSide();

            buyList.add(pack);
        }

        tmp = tradeHistoryRepository.findAllBySellTraderID(traderID);
        for(TradeHistory h : tmp){
            Future future = futureRepository.getOne(h.getFutureID());
            TradeHistoryPack2 pack = new TradeHistoryPack2();

            pack.amount = h.getAmount();
            pack.buyTraderID = h.getBuyTraderID();
            pack.buyTraderOrderID = h.getBuyTraderOrderID();
            pack.futureID = h.getFutureID();
            pack.product = future.getFutureName();
            pack.sellTraderID = h.getSellTraderID();
            pack.sellTraderOrderID = h.getSellTraderOrderID();
            pack.timeStamp = h.getTimeStamp();
            pack.tradeID = h.getTradeID();
            pack.price = h.getPrice();
            pack.period = future.getPeriod();
            pack.initiatorSide = h.getInitiatorSide();

            sellList.add(pack);
        }
        result.put("buy_list", buyList);
        result.put("sell_list", sellList);
        return result;
    }

    @Override
    public List<TradeHistoryPack> getHistoryByFuture(String futureName, String period) {
        Future future = futureRepository.findByFutureNameAndPeriod(futureName, period);
        List<TradeHistory> tradeHistoryList = tradeHistoryRepository.findAllByFutureID(future.getFutureID());
        List<TradeHistoryPack> result = new LinkedList<>();
        for(TradeHistory tradeHistory : tradeHistoryList){
            TradeHistoryPack pack = new TradeHistoryPack();
            pack.buyer_name = traderRepository.findById(tradeHistory.getBuyTraderID()).get().getTraderName();
            pack.seller_name = traderRepository.findById(tradeHistory.getSellTraderID()).get().getTraderName();
            pack.price = tradeHistory.getPrice();
            pack.qty = tradeHistory.getAmount();
            pack.tradeID = tradeHistory.getTradeID();
            pack.period = period;
            pack.initiator_side = tradeHistory.getInitiatorSide();
            pack.future_name = future.getFutureName();
            pack.buyer_order_id = tradeHistory.getBuyTraderOrderID();
            pack.seller_order_id = tradeHistory.getSellTraderOrderID();
            result.add(pack);
        }

        return result;
    }

    @Override
    public List<TradeHistoryPack> getAllHistory() {
        List<TradeHistory> historyList = tradeHistoryRepository.findAll();
        List<TradeHistoryPack> result = new LinkedList<>();
        for(TradeHistory h : historyList){
            Trader buyer = traderRepository.getOne(h.getBuyTraderID());
            Trader seller = traderRepository.getOne(h.getSellTraderID());
            Future future = futureRepository.getOne(h.getFutureID());
            TradeHistoryPack pack = new TradeHistoryPack();

            pack.initiator_side = h.getInitiatorSide();
            pack.tradeID = h.getTradeID();
            pack.price = h.getPrice();
            pack.seller_name = seller.getTraderName();
            pack.buyer_name = buyer.getTraderName();
            pack.future_name = future.getFutureName();
            pack.period = future.getPeriod();
            pack.buyer_order_id = h.getBuyTraderOrderID();
            pack.seller_order_id = h.getSellTraderOrderID();
            pack.qty = h.getAmount();
            pack.time = h.getTimeStamp();

            result.add(pack);

        }

        return result;
    }
}
