package group.eis.morganborker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
public class HistoryController {
    @RequestMapping(method = RequestMethod.GET)
    public String getHistory(){
        return "fake history";
    }
}
