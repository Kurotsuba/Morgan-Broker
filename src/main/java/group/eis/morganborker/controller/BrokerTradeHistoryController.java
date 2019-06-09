package group.eis.morganborker.controller;

import group.eis.morganborker.entity.TradeHistory;
import group.eis.morganborker.service.TradeHistoryService;
import group.eis.morganborker.utils.TradeHistoryPack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/broker_tradehistory")
public class BrokerTradeHistoryController {
    @Autowired
    private TradeHistoryService tradeHistoryService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TradeHistoryPack> getTradeHistoryByFuture(@RequestParam String futureName, @RequestParam String period){
        return tradeHistoryService.getHistoryByFuture(futureName, period);
    }
}
