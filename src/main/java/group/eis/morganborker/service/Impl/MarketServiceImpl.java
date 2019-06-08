package group.eis.morganborker.service.Impl;

import com.google.gson.Gson;
import group.eis.morganborker.service.MarketService;
import group.eis.morganborker.utils.RedisUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service("MarketService")
public class MarketServiceImpl implements MarketService {
    private final RedisUtil redisUtil;
    private final Gson gson;

    public MarketServiceImpl(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
        gson = new Gson();
    }
    @Override
    public HashMap<String, HashMap<String, Integer>> getMarket(Long futureID) {
        Set<Object> buyKeySet = redisUtil.hashKeys(futureID.toString()+"_buy_list");
        Set<Object> sellKeySet = redisUtil.hashKeys(futureID.toString()+"_sell_list");

        HashMap<String, HashMap<String, Integer>> resultMap = new HashMap<>();
        HashMap<String, Integer> buyMap = new HashMap<>();
        HashMap<String, Integer> sellMap = new HashMap<>();
        for (Object key: buyKeySet) {
            buyMap.put((String)key, (Integer) redisUtil.hashGet(futureID.toString()+"_buy_list", (String)key));
        }

        for (Object key: sellKeySet) {
            sellMap.put((String)key, (Integer) redisUtil.hashGet(futureID.toString()+"_sell_list", (String)key));
        }


        resultMap.put("buy_list", buyMap);
        resultMap.put("sell_list", sellMap);
        return resultMap;
    }
}
