package group.eis.morganborker.utils;

import com.google.gson.Gson;
import group.eis.morganborker.model.Order;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderParser {
    Gson gson;
    OrderParser(){
        gson = new Gson();
    }

    Order getOrder(String jsonString){
        OrderPojo opj = gson.fromJson(jsonString, OrderPojo.class);
        return new Order(opj.order_id, opj.future_id, opj.type, opj.side, opj.price, opj.price2, opj.amount, opj.broker_name, opj.time_stamp);
    }

    String getJson(Order o){
        OrderPojo opj = new OrderPojo(o.getOrderID(), o.getFutureID(), o.getType(), o.getSide(), o.getPrice(), o.getPrice2(), o.getAmount(), o.getBrokerName(), o.getTimeStamp());
        return gson.toJson(opj);
    }

}

class OrderPojo {
    public UUID order_id;
    public String future_id;
    public char type;
    public char side;
    public Double price;
    public Double price2;
    public Double amount;
    public String broker_name;
    public LocalDateTime time_stamp;

    public OrderPojo(UUID order_id, String future_id, char type, char side, Double price, Double price2, Double amount, String broker_name, LocalDateTime time_stamp) {
        this.order_id = order_id;
        this.future_id = future_id;
        this.type = type;
        this.side = side;
        this.price = price;
        this.price2 = price2;
        this.amount = amount;
        this.broker_name = broker_name;
        this.time_stamp = time_stamp;
    }
}
