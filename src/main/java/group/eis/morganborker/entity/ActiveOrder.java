package group.eis.morganborker.entity;

import org.omg.CORBA.INTERNAL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "active_order")
public class ActiveOrder {
    @Id
    @Column(nullable = false, name = "order_id")
    private Long orderID;

    @Column(name = "rest_amount")
    private Integer restAmount;

    public ActiveOrder(Long orderID, Integer restAmount) {
        this.orderID = orderID;
        this.restAmount = restAmount;
    }

    public Integer getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(Integer restAmount) {
        this.restAmount = restAmount;
    }

    public Long getOrderID() {
        return orderID;
    }
}
