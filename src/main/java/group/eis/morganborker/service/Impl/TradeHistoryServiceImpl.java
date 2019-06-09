package group.eis.morganborker.service.Impl;

import group.eis.morganborker.entity.TradeHistory;
import group.eis.morganborker.repository.FutureRepository;
import group.eis.morganborker.repository.TradeHistoryRepository;
import group.eis.morganborker.repository.TraderRepository;
import group.eis.morganborker.service.TradeHistoryService;
import group.eis.morganborker.utils.TradeHistoryPack;
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

    @Override
    public Map<String, List<TradeHistory>> getHistory(Long traderID) {
        Map<String, List<TradeHistory>> result = new HashMap<>();
        result.put("buy_list", tradeHistoryRepository.findAllByBuyTraderID(traderID));
        result.put("sell_list", tradeHistoryRepository.findAllBySellTraderID(traderID));
        return result;
    }

    @Override
    public List<TradeHistoryPack> getHistoryByFuture(String futureName, String period) {
        Long futureID = futureRepository.findByFutureNameAndPeriod(futureName, period).getFutureID();
        List<TradeHistory> tradeHistoryList = tradeHistoryRepository.findAllByFutureID(futureID);
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
            result.add(pack);
        }

        return result;
    }
}
