package group.eis.morganborker.service;

import group.eis.morganborker.entity.Order;

public interface TradeService {
    public Integer deal(Order order1, Order order2);
}
