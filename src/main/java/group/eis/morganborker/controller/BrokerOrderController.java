package group.eis.morganborker.controller;

import group.eis.morganborker.entity.Order;
import group.eis.morganborker.repository.FutureRepository;
import group.eis.morganborker.repository.OrderRepository;
import group.eis.morganborker.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/broker_order")
public class BrokerOrderController {
    @Autowired
    private FutureRepository futureRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Order> getOrderByFuture(@RequestParam String futureName, @RequestParam String period){
        return orderService.findOrderByFuture(futureName, period);
    }
}
