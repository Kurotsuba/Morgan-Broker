package group.eis.morganborker.utils;

import group.eis.morganborker.entity.TradeHistory;

public class TradeHistoryPack {
    public Long tradeID;
    public String future_name;
    public String period;
    public Integer price;
    public Integer qty;
    public String buyer_name;
    public String seller_name;
    public char initiator_side;

    public TradeHistoryPack() {
    }
}
