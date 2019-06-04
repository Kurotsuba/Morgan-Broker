package group.eis.morganborker.utils;

import com.google.gson.Gson;
import group.eis.morganborker.entity.Order;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderParser {
    Gson gson;
    public OrderParser(){
        gson = new Gson();
    }

    public Order getOrder(String jsonString){
        OrderPojo opj = gson.fromJson(jsonString, OrderPojo.class);
        return new Order(opj.future_id, opj.type, opj.side, opj.price, opj.price2, opj.amount, opj.trader_name, opj.trader_id, opj.time_stamp);
    }


}

class OrderPojo {
    public Long order_id;
    public Long future_id;
    public char type; // 'm','f','s','c'
    public char side; // 'b','s'
    public Integer price;
    public Integer price2;
    public Integer amount;
    public Long trader_id;
    public String trader_name;
    public LocalDateTime time_stamp;

    public OrderPojo(Long order_id, Long future_id, char type, char side, Integer price, Integer price2, Integer amount, Long trader_id, String trader_name, LocalDateTime time_stamp) {
        this.order_id = order_id;
        this.future_id = future_id;
        this.type = type;
        this.side = side;
        this.price = price;
        this.price2 = price2;
        this.amount = amount;
        this.trader_id = trader_id;
        this.trader_name = trader_name;
        this.time_stamp = time_stamp;
    }

    public OrderPojo(Long future_id, char type, char side, Integer price, Integer price2, Integer amount, Long trader_id, String trader_name, LocalDateTime time_stamp) {
        this.future_id = future_id;
        this.type = type;
        this.side = side;
        this.price = price;
        this.price2 = price2;
        this.amount = amount;
        this.trader_id = trader_id;
        this.trader_name = trader_name;
        this.time_stamp = time_stamp;
    }
}
