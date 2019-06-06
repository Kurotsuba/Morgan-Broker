package group.eis.morganborker.utils;

import group.eis.morganborker.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderQueueUtil {
    @Autowired
    RedisUtil redisUtil;

    /*
    * maintain several redis hashmaps for market depth
    * name = {futureID}_[buy/sell]_list
    * key = price
    * value = amount
     */
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

    public Integer getAmount(Long futureID, char side, Integer price){
        if(side == 'b'){
            return (Integer)redisUtil.hashGet(futureID.toString()+"_buy_list", price.toString());
        }else if(side == 's'){
            return (Integer)redisUtil.hashGet(futureID.toString()+"_sell_list", price.toString());
        }
        return -1;
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

    /*
    * maintain several redis list for time queue for order
    * name = {side}_{price}
    * value = order
     */

    public boolean addOrder(Order order){
        return redisUtil.listPush(order.getSide() + "_" + order.getPrice().toString(), order);
    }

    public Order getOrder(char side, Integer price){
        return (Order)redisUtil.listGetOne(side + "_" + price.toString(), 0);
    }

    public Order popOrder(char side, Integer price){
        return (Order)redisUtil.listPop(side + "_" + price.toString());
    }

    public void headOrderUpdate(char side, Integer price, Integer value){
        redisUtil.listUpdate(side + "_" + price.toString(), 0, value);
    }

    /*
    * maintain a redis set for cancel order
    * name = "cancel_list"
    * value = order_id
     */
    public void addCancelOrder(Order order){
        redisUtil.setAdd("cancel_list", order.getOrderID());
    }

    public boolean findStopOrder(Long order_id){
        return redisUtil.setIsMember("cancel_list", order_id);
    }

    public void deleteStopOrder(Long stop_id){
        redisUtil.setRemove("cancel_list", stop_id);
    }

}
