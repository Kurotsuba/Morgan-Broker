package group.eis.morganborker.controller;

import group.eis.morganborker.entity.Order;
import group.eis.morganborker.utils.OrderParser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @RequestMapping(method = RequestMethod.POST)
    public String order(String request){
        OrderParser op = new OrderParser();
        Order currentOrder = op.getOrder(request);
        switch (currentOrder.getType()){
            case 'm':break;
            case 'f':break;
            case 's':break;
            case 'c':break;
            default:break;
        }
        return "Success";

    }

    @RequestMapping(method = RequestMethod.GET)
    public String getOrder(){
        return "fake order";
    }

}
