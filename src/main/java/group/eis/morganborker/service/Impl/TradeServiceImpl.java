package group.eis.morganborker.service.Impl;

import group.eis.morganborker.entity.Order;
import group.eis.morganborker.service.TradeService;
import org.springframework.stereotype.Service;

@Service("TradeService")
public class TradeServiceImpl implements TradeService {

    @Override
    public Integer deal(Order order1, Order order2) {
        return 0;
    }
}
