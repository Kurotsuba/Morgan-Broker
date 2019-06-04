package group.eis.morganborker.controller;

import group.eis.morganborker.entity.Order;
import group.eis.morganborker.utils.OrderParser;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/order")
public class OrderController {
    @ApiOperation(value = "recieve a order", notes = "")
    @RequestMapping(method = RequestMethod.POST)
    public String order(@RequestBody Order order){
        switch (order.getType()){
            case 'm':break;
            case 'f':break;
            case 's':break;
            case 'c':break;
            default:break;
        }
        return "Success";

    }

    @ApiOperation(value = "get a order with order_id")
    @RequestMapping(method = RequestMethod.GET)
    public Order getOrder(@RequestParam String ID){
        Order  fakeOrder = new Order(0l, 't', 't', 0, 0, 0, "test", 0l, LocalDateTime.now());
        return fakeOrder;
    }

}
