package group.eis.morganborker.controller;

import group.eis.morganborker.entity.Future;
import group.eis.morganborker.service.MarketService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/market")
public class MarketController {
    @Autowired
    private MarketService marketService;
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "get market depth with future id")
    public HashMap<String, HashMap<String, Integer>> getHistory(@RequestParam Long futureID){
        return marketService.getMarket(futureID, -1);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "get list of market depth")
    public List<HashMap<String, HashMap<String, Integer>>> getAllMarket(@RequestBody List<FutureIDPOJO> idList){
        List<HashMap<String, HashMap<String, Integer>>> result = new LinkedList<>();
        for(FutureIDPOJO pojo : idList){
            result.add(marketService.getMarket(pojo.future_id, -1));
        }
        return result;
    }
}

class FutureIDPOJO{
    public Long future_id;
}
