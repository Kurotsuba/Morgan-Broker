package group.eis.morganborker.entity;

import javax.persistence.*;

@Entity
@Table(name = "trade_history")
public class TradeHistory {
    @Id
    @GeneratedValue
    @Column(nullable = false, name = "trade_id")
    private Long tradeID;

    @Column(nullable = false, name = "order_id")
    private Long orderID;

    @Column(nullable = false, name = "amount")
    private Integer amount;

    @Column(nullable = false, name = "price")
    private Integer price;

    public TradeHistory(Long orderID, Integer amount, Integer price) {
        this.orderID = orderID;
        this.amount = amount;
        this.price = price;
    }

    public Long getTradeID() {
        return tradeID;
    }

    public void setTradeID(Long tradeID) {
        this.tradeID = tradeID;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
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
