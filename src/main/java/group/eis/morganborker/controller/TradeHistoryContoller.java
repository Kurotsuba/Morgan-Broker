package group.eis.morganborker.controller;

import group.eis.morganborker.entity.TradeHistory;
import group.eis.morganborker.service.TradeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/history")
public class TradeHistoryContoller {
    @Autowired
    private TradeHistoryService tradeHistoryService;

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, List<TradeHistory>> getTradeHistory(@RequestParam Long traderID){
        return tradeHistoryService.getHistory(traderID);
    }
}
