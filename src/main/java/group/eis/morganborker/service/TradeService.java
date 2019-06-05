package group.eis.morganborker.service;

import group.eis.morganborker.entity.ActiveOrder;

public interface TradeService {
    public void deal(ActiveOrder order);
}
