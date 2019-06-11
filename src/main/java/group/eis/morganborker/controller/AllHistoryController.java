package group.eis.morganborker.controller;

import group.eis.morganborker.service.TradeHistoryService;
import group.eis.morganborker.utils.TradeHistoryPack;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/get_all_history")
public class AllHistoryController {
    @Autowired
    TradeHistoryService tradeHistoryService;

    @ApiOperation(value = "get all trade history")
    @RequestMapping(method = RequestMethod.GET)
    public List<TradeHistoryPack> getAllHistory(){
        return tradeHistoryService.getAllHistory();
    }
}
