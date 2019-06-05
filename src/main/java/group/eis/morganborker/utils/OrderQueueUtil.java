package group.eis.morganborker.utils;

import group.eis.morganborker.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderQueueUtil {
    @Autowired
    RedisUtil redisUtil;

    public boolean addAmount(Order order){
        String key = order.getFutureID().toString();
        if(order.getSide() == 'b'){
            key = key + "_buy_list";
        }else if(order.getSide() == 's'){
            key = key + "_sell_list";
        }
        if(!redisUtil.hasKey(key)){
            HashMap<String, Object> buyMapping = new HashMap<>();
            buyMapping.put(order.getPrice().toString(), order.getAmount());
            return redisUtil.hashMapSet(key, buyMapping);
        }
        if(redisUtil.hashKeys(key).contains(order.getPrice().toString())){
            return redisUtil.hashIncrement(key, order.getPrice().toString(), order.getAmount()) != 0l;
        }else{
            return redisUtil.hashSet(key, order.getPrice().toString(), order.getAmount());
        }
    }


    public boolean addOrder(Order order){
        return redisUtil.listPush(order.getPrice().toString(), order.getOrderID());
    }

    public Order getOrder(Integer price){
        return (Order)redisUtil.listGetOne(price.toString(), 0);
    }

    public Order popOrder(Integer price){
        return (Order)redisUtil.listPop(price.toString());
    }

    public void headOrderUpdate(Integer price, Integer value){
        redisUtil.listUpdate(price.toString(), 0, value);
    }

    public Integer getLowestPrice(Order order){
        Set<Object> prices = redisUtil.hashKeys(order.getFutureID().toString()+"_sell_list");
        if(prices.isEmpty()){
            return -1;
        }
        Set<Integer> numPrices = new HashSet<>();
        for (Object key : prices) {
            numPrices.add(Integer.valueOf((String)key));
        }

        return Collections.min(numPrices);
    }

    public Integer getHighestPrice(Order order){
        Set<Object> prices = redisUtil.hashKeys(order.getFutureID().toString()+"_buy_list");
        if(prices.isEmpty()){
            return -1;
        }
        Set<Integer> numPrices = new HashSet<>();
        for (Object key : prices) {
            numPrices.add(Integer.valueOf((String)key));
        }

        return Collections.max(numPrices);
    }
}
