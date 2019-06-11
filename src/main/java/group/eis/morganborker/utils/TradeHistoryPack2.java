package group.eis.morganborker.utils;

import java.time.LocalDateTime;

public class TradeHistoryPack2 {
    public Long tradeID;
    public Long buyTraderOrderID;
    public Long buyTraderID;
    public Long sellTraderOrderID;
    public Long sellTraderID;
    public Integer amount;
    public Integer price;
    public LocalDateTime timeStamp;
    public char initiatorSide;
    public Long futureID;
    public String product;
    public String period;

}
