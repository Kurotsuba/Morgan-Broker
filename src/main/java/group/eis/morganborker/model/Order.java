package group.eis.morganborker.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Order {
    private UUID orderID;
    private String futureID;
    private char type;
    private char side;
    private Double price;
    private Double price2;
    private Double amount;
    private String brokerName;
    private LocalDateTime timeStamp;

    public Order(UUID orderID, String futureID, char type, char side, Double price, Double price2, Double amount, String brokerName, LocalDateTime timeStamp) {
        this.orderID = orderID;
        this.futureID = futureID;
        this.type = type;
        this.side = side;
        this.price = price;
        this.price2 = price2;
        this.amount = amount;
        this.brokerName = brokerName;
        this.timeStamp = timeStamp;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public String getFutureID() {
        return futureID;
    }

    public char getType() {
        return type;
    }

    public char getSide() {
        return side;
    }

    public Double getPrice() {
        return price;
    }

    public Double getPrice2() {
        return price2;
    }

    public Double getAmount() {
        return amount;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }


}
