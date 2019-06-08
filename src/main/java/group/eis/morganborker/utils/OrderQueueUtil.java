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
        System.out.println("add amount "+order.getOrderID());
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
            return redisUtil.hashDelta(key, order.getPrice().toString(), order.getAmount()) != 0l;
        }else{
            return redisUtil.hashSet(key, order.getPrice().toString(), order.getAmount());
        }
    }

    public boolean decreseAmount(Order order){
        String key = order.getFutureID().toString();
        if(order.getSide() == 'b'){
            key = key + "_buy_list";
        }else if(order.getSide() == 's'){
            key = key + "_sell_list";
        }
        boolean result = redisUtil.hashDelta(key, order.getPrice().toString(), -order.getAmount()) > 0;
        if((Integer) redisUtil.hashGet(key, order.getPrice().toString()) == 0){
            redisUtil.hashDel(key, order.getPrice().toString());
        }
        return result;
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
    * maintain several redis list for time queue of order
    * name = {future_id}_{side}_{price}
    * value = order_id
     */

    public boolean addOrder(Order order){
        return redisUtil.listPush(order.getFutureID().toString()+ "_" +order.getSide() + "_" + order.getPrice().toString(), order.getOrderID());
    }

    public Long getOrder(Long futureID, char side, Integer price){
        Integer orderID = (Integer)redisUtil.listGetOne(futureID.toString()+ "_" + side + "_" + price.toString(), 0);
        return orderID.longValue();
    }

    public Long popOrder(Long futureID, char side, Integer price){
        Integer orderID = (Integer)redisUtil.listPop(futureID.toString()+ "_" + side + "_" + price.toString());
        return orderID.longValue();
    }

    public void headOrderUpdate(char side, Integer price, Integer value){
        redisUtil.listUpdate(side + "_" + price.toString(), 0, value);
    }

    /*
    * maintain a redis set for cancel order
    * name = "cancel_list"
    * value = {order_id}
     */
    public void addCancelOrder(Order order){
        redisUtil.setAdd("cancel_list", order.getOrderID());
    }

    public boolean findCancelOrder(Long order_id){
        return redisUtil.setIsMember("cancel_list", order_id);
    }

    public void deleteCancelOrder(Long stop_id){
        redisUtil.setRemove("cancel_list", stop_id);
    }

    /*
    * maintain several redis list for stop order
    * name = stop_{future_id}_{side}_{price}
    * value = {order_id}
    *
    * maintain 2 set for stop order price
    * name = "{future_id}_{side}_stop_set"
    * value = {price}
     */
    public void addStopOrder(Order order){
        redisUtil.listPush("stop_"+order.getFutureID().toString()+"_"+ order.getSide() + "_" + order.getPrice().toString(), order.getOrderID());
        redisUtil.setAdd(order.getFutureID().toString() + "_" + order.getSide() + "_stop_set", order.getPrice());
    }

    public List<Long> getStopOrderList(Long futureID, char side, Integer price){
        List<Long> result = new LinkedList<>();
        List<Object> orderList = redisUtil.listGet("stop_"+futureID.toString()+"_"+side+"_"+price.toString(), 0, -1);
        for(Object o : orderList){
            result.add(((Integer)o).longValue());
        }
        redisUtil.setRemove(futureID.toString()+"_"+side+"_stop_set", price);
        return result;
    }

    public Integer getHighestStopPrice(Long futureID){
        Set<Object> prices = redisUtil.setGet(futureID.toString()+"_b_stop_set");
        if(prices.isEmpty()){
            return -1;
        }
        Set<Integer> numPrices = new HashSet<>();
        for (Object key : prices) {
            numPrices.add((Integer)key);
        }

        return Collections.max(numPrices);
    }
    public Integer getLowestStopPrice(Long futureID){
        Set<Object> prices = redisUtil.setGet(futureID.toString()+"_s_stop_set");
        if(prices.isEmpty()){
            return -1;
        }
        Set<Integer> numPrices = new HashSet<>();
        for (Object key : prices) {
            numPrices.add((Integer)key);
        }

        return Collections.min(numPrices);
    }






}
