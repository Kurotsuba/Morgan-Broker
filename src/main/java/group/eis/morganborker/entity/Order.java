package group.eis.morganborker.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "[order]")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "order_id")
    private Long orderID;

    @Column(name = "trader_order_id")
    private Long traderOrderID;

    @Column(nullable = false, name = "future_id")
    private Long futureID;

    @Column(nullable = false, name = "type")
    private char type;

    @Column(nullable = false, name = "side")
    private char side;

    @Column(nullable = false, name = "price")
    private Integer price;

    @Column(name = "price2")
    private Integer price2;

    @Column(nullable = false, name = "amount")
    private Integer amount;

    @Column(name = "trader_name")
    private String traderName;

    @Column(nullable = false, name = "trader_id")
    private Long traderID;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    public Order(){

    }

    public Order(Long futureID, char type, char side, Integer price, Integer price2, Integer amount, String traderName, Long traderID, LocalDateTime timeStamp) {
        this.futureID = futureID;
        this.type = type;
        this.side = side;
        this.price = price;
        this.price2 = price2;
        this.amount = amount;
        this.traderName = traderName;
        this.traderID = traderID;
        this.timeStamp = LocalDateTime.now();
    }

    public Order(Long traderOrderID, Long futureID, char type, char side, Integer price, Integer price2, Integer amount, String traderName, Long traderID, LocalDateTime timeStamp) {
        this.traderOrderID = traderOrderID;
        this.futureID = futureID;
        this.type = type;
        this.side = side;
        this.price = price;
        this.price2 = price2;
        this.amount = amount;
        this.traderName = traderName;
        this.traderID = traderID;
        this.timeStamp = LocalDateTime.now();
    }

    public Order(TraderOrder traderOrder){
        this.amount = traderOrder.amount;
        this.futureID = traderOrder.futureID;
        this.traderOrderID = traderOrder.orderID;
        this.price = traderOrder.price;
        this.price2 = traderOrder.price2;
        this.traderName = traderOrder.traderName;
        this.type = traderOrder.type;
        this.side = traderOrder.side;
        this.traderID = traderOrder.traderID;
        this.timeStamp = LocalDateTime.now();
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Long getTraderOrderID() {
        return traderOrderID;
    }

    public void setTraderOrderID(Long traderOrderID) {
        this.traderOrderID = traderOrderID;
    }

    public Long getFutureID() {
        return futureID;
    }

    public void setFutureID(Long futureID) {
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    public Long getTraderID() {
        return traderID;
    }

    public void setTraderID(Long traderID) {
        this.traderID = traderID;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
