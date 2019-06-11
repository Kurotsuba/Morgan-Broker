package group.eis.morganborker.controller;

import group.eis.morganborker.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/active_order")
public class ActiveOrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ActiveOrderPOJO> getActiveOrdersByFuture(@RequestParam String futureName, @RequestParam String period){
        return orderService.findActiveOrderByFuture(futureName, period);
    }
}

