package group.eis.morganborker.entity;

public class TraderOrder {
    public Long traderID;
    public Long orderID;
    public Long futureID;
    public char type;
    public char side;
    public Integer price;
    public Integer price2;
    public Integer amount;
    public String traderName;

    public TraderOrder() {
    }
}
