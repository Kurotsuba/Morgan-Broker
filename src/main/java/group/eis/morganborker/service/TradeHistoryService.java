package group.eis.morganborker.service;

import group.eis.morganborker.utils.TradeHistoryPack;
import group.eis.morganborker.utils.TradeHistoryPack2;

import java.util.List;
import java.util.Map;

public interface TradeHistoryService {
    Map<String, List<TradeHistoryPack2>> getHistory(Long traderID);
    List<TradeHistoryPack> getHistoryByFuture(String futureName, String period);
    List<TradeHistoryPack> getAllHistory();
}
