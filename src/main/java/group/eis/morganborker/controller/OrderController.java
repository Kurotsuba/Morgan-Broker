package group.eis.morganborker.controller;

import group.eis.morganborker.entity.Order;
import group.eis.morganborker.entity.TraderOrder;
import group.eis.morganborker.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private  OrderService orderService;
    @ApiOperation(value = "send a order", notes = "")
    @RequestMapping(method = RequestMethod.POST)
    public Long order(@RequestBody TraderOrder order){
        return orderService.recieveOrder(order);

    }

    @ApiOperation(value = "get a order with traderID and trader generated orderID")
    @RequestMapping(method = RequestMethod.GET)
    public Order getOrder(@RequestParam Long traderID, @RequestParam Long traderOrderID){
        return orderService.findOrder(traderID, traderOrderID);
    }

    @ApiOperation(value = "get a order with broker generated orderID")
    public Order getOrder(@RequestParam Long orderID){
        return orderService.findOrder(orderID);
    }

}
