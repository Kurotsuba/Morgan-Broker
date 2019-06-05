package group.eis.morganborker.controller;

import group.eis.morganborker.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
public class MarketController {
    @Autowired
    private MarketService marketService;
    @RequestMapping(method = RequestMethod.GET)
    public String getHistory(@RequestParam Long futureID){
        return marketService.getMarket(futureID);
    }
}
