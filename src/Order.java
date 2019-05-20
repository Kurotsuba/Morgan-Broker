import java.util.Date;

public class Order {
    private String orderType;
    private Date submitTime;
    private int amount;
    private int price;
    private long traderID;
    private String traderType;
    private long orderID;
    private String status;
    private long commodityID;

    public Order(String orderType, Date submitTime, int amount, int price, long traderID, String traderType, long orderID, String status, long commodityID) {
        this.orderType = orderType;
        this.submitTime = submitTime;
        this.amount = amount;
        this.price = price;
        this.traderID = traderID;
        this.traderType = traderType;
        this.orderID = orderID;
        this.status = status;
        this.commodityID = commodityID;
    }

    public String getOrderType() {
        return orderType;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public long getTraderID() {
        return traderID;
    }

    public String getTraderType() {
        return traderType;
    }

    public long getOrderID() {
        return orderID;
    }

    public String getStatus() {
        return status;
    }

    public long getCommodityID() {
        return commodityID;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
