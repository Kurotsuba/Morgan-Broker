package group.eis.morganborker.service;

import java.util.HashMap;

public interface MarketService {
    HashMap<String, HashMap<String, Integer>> getMarket(Long futureID, int count);
}
