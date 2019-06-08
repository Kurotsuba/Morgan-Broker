package group.eis.morganborker.service;

import group.eis.morganborker.entity.TradeHistory;

import java.util.List;
import java.util.Map;

public interface TradeHistoryService {
    Map<String, List<TradeHistory>> getHistory(Long traderID);
}
