package group.eis.morganborker.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue
    @Column(nullable = false, name = "order_id")
    private Long orderID;

    @Column(nullable = false, name = "future_id")
    private long futureID;

    @Column(nullable = false, name = "type")
    private char type;

    @Column(nullable = false, name = "side")
    private char side;

    @Column(nullable = false, name = "price")
    private double price;

    @Column(nullable = false, name = "amount")
    private int amount;

    @Column(nullable = false, name = "broker_name")
    private String brokerName;

    private LocalDateTime timeStamp;

    public Order(long futureID, char type, char side, double price, int amount, String brokerName, LocalDateTime timeStamp) {
        this.futureID = futureID;
        this.type = type;
        this.side = side;
        this.price = price;
        this.amount = amount;
        this.brokerName = brokerName;
        this.timeStamp = timeStamp;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public long getFutureID() {
        return futureID;
    }

    public void setFutureID(long futureID) {
        this.futureID = futureID;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public char getSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
