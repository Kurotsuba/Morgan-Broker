package group.eis.morganborker.utils;


import group.eis.morganborker.model.Order;

import java.util.Vector;

public class MarketDepth {
    private Vector<Entity> buyerList;
    private Vector<Entity> sellerList;
    public MarketDepth(){
        buyerList = new Vector<>();
        sellerList = new Vector<>();
    }

    public boolean updateMD(Order order){
        if(order.getType() == 's'){
            if(sellerList.size() == 0){
                sellerList.add(new Entity(order.getPrice(), order.getAmount()));
                return true;
            }
            return true;
        }else if(order.getType() == 'b'){
            if(buyerList.size() == 0){
                buyerList.add(new Entity(order.getPrice(), order.getAmount()));
            }
            return true;
        }else{
            return false;
        }
    }

    public Vector<Entity> getBuyerList() {
        return buyerList;
    }

    public Vector<Entity> getSellerList() {
        return sellerList;
    }
}

class Entity{
    public Double price;
    public Double amount;
    Entity(Double price, Double amount){
        this.price = price;
        this.amount = amount;
    }
}
