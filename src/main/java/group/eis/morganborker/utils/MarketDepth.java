package group.eis.morganborker.utils;


import group.eis.morganborker.entity.Order;

import java.util.Vector;

public class MarketDepth {
    private Vector<Entity> buyerList;
    private Vector<Entity> sellerList;
    public MarketDepth(){
        buyerList = new Vector<>();
        sellerList = new Vector<>();
    }

    private Entity buySellingOrder(Entity request){
        if(sellerList.size() == 0){
            return request;
        }

        for (int i = 0; i < sellerList.size(); i++) {
            int listAmount = sellerList.get(i).amount;
            int requestAmount = request.amount;
            if(sellerList.get(i).price < request.price){
                if(listAmount < requestAmount){
                    request.amount -= sellerList.get(i).amount;
                    sellerList.remove(i);
                    // TODO: record trade success
                }else if(listAmount == requestAmount){
                    sellerList.remove(i);
                    request.amount = 0;
                    return request;
                    // TODO: record trade success
                }else{
                    sellerList.get(i).amount -= request.amount;
                    return request;
                    // TODO: record trade success
                }
            }else{
                return request;
            }
        }
        return request;
    }

    private Entity sellBuyingOrder(Entity request){
        if(buyerList.size() == 0){
            return request;
        }

        for (int i = 0; i < buyerList.size(); i++) {
            int listAmount = buyerList.get(i).amount;
            int requestAmount = request.amount;
            if(buyerList.get(i).price > request.price){
                if(listAmount > requestAmount){
                    request.amount -= buyerList.get(i).amount;
                    buyerList.remove(i);
                    // TODO: record trade success
                }else if(listAmount == requestAmount){
                    buyerList.remove(i);
                    request.amount = 0;
                    return request;
                    // TODO: record trade success
                }else{
                    buyerList.get(i).amount -= request.amount;
                    return request;
                    // TODO: record trade success
                }
            }else{
                return request;
            }
        }
        return request;

    }

    public boolean updateMD(Order order){
        double oPrice = order.getPrice();
        int oAmount = order.getAmount();
        if(order.getSide() == 's'){
            Entity rest = sellBuyingOrder(new Entity(oPrice, oAmount));
            if(sellerList.size() == 0){
                sellerList.add(rest);
                return true;
            }
            for (int i = 0; i < sellerList.size(); i++) {
                if(sellerList.get(i).price == rest.price){
                    sellerList.get(i).amount += rest.amount;
                    return true;
                }else if(sellerList.get(i).price > rest.price) {
                    sellerList.add(i, rest);
                    return true;
                }
            }
            sellerList.add(rest);
            return true;
        }else if(order.getSide() == 'b'){
            Entity rest = buySellingOrder(new Entity(oPrice, oAmount));
            if(buyerList.size() == 0){
                buyerList.add(rest);
                return true;
            }
            for (int i = 0; i < buyerList.size(); i++) {
                if(buyerList.get(i).price == rest.price){
                    buyerList.get(i).amount += rest.amount;
                    return true;
                }else if(buyerList.get(i).price < rest.price) {
                    buyerList.add(i, rest);
                    return true;
                }
            }
            buyerList.add(rest);
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
    public double price;
    public int amount;
    Entity(double price, int amount){
        this.price = price;
        this.amount = amount;
    }
}
