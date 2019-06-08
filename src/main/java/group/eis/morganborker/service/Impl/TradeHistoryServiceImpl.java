package group.eis.morganborker.service.Impl;

import group.eis.morganborker.entity.TradeHistory;
import group.eis.morganborker.repository.TradeHistoryRepository;
import group.eis.morganborker.service.TradeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("TradeHistoryService")
public class TradeHistoryServiceImpl implements TradeHistoryService {
    @Autowired
    TradeHistoryRepository tradeHistoryRepository;

    @Override
    public Map<String, List<TradeHistory>> getHistory(Long traderID) {
        Map<String, List<TradeHistory>> result = new HashMap<>();
        result.put("buy_list", tradeHistoryRepository.findAllByBuyTraderID(traderID));
        result.put("sell_list", tradeHistoryRepository.findAllBySellTraderID(traderID));
        return result;
    }
}
