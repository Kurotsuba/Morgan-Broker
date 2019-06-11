package group.eis.morganborker.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trade_history")
public class TradeHistory {
    @Id
    @GeneratedValue
    @Column(nullable = false, name = "trade_id")
    private Long tradeID;

    @Column(nullable = false, name = "buy_trader_order_id")
    private Long buyTraderOrderID;

    @Column(nullable = false, name = "buy_trader_id")
    private Long buyTraderID;

    @Column(nullable = false, name = "sell_trader_order_id")
    private Long sellTraderOrderID;

    @Column(nullable = false, name = "sell_trader_id")
    private Long sellTraderID;

    @Column(nullable = false, name = "amount")
    private Integer amount;

    @Column(nullable = false, name = "price")
    private Integer price;

    @Column(nullable = false, name = "time_stamp")
    private LocalDateTime timeStamp;

    @Column(nullable = false, name = "initiator_side")
    private char initiatorSide;

    @Column(nullable = false, name = "future_id")
    private Long futureID;

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public TradeHistory(Long buyTraderOrderID, Long buyTraderID, Long sellTraderOrderID, Long sellTraderID, Integer amount, Integer price, char initiatorSide, Long futureID) {
        this.buyTraderOrderID = buyTraderOrderID;
        this.buyTraderID = buyTraderID;
        this.futureID = futureID;
        this.sellTraderOrderID = sellTraderOrderID;
        this.sellTraderID = sellTraderID;
        this.amount = amount;
        this.price = price;
        this.timeStamp = LocalDateTime.now();
        this.initiatorSide = initiatorSide;
    }

    public Long getFutureID() {
        return futureID;
    }

    public void setFutureID(Long futureID) {
        this.futureID = futureID;
    }

    public char getInitiatorSide() {
        return initiatorSide;
    }

    public void setInitiatorSide(char initiatorSide) {
        this.initiatorSide = initiatorSide;
    }

    public TradeHistory() {
    }

    public Long getTradeID() {
        return tradeID;
    }

    public void setTradeID(Long tradeID) {
        this.tradeID = tradeID;
    }

    public Long getBuyTraderOrderID() {
        return buyTraderOrderID;
    }

    public void setBuyTraderOrderID(Long buyTraderOrderID) {
        this.buyTraderOrderID = buyTraderOrderID;
    }

    public Long getBuyTraderID() {
        return buyTraderID;
    }

    public void setBuyTraderID(Long buyTraderID) {
        this.buyTraderID = buyTraderID;
    }

    public Long getSellTraderOrderID() {
        return sellTraderOrderID;
    }

    public void setSellTraderOrderID(Long sellTraderOrderID) {
        this.sellTraderOrderID = sellTraderOrderID;
    }

    public Long getSellTraderID() {
        return sellTraderID;
    }

    public void setSellTraderID(Long sellTraderID) {
        this.sellTraderID = sellTraderID;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
