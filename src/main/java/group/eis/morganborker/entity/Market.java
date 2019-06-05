package group.eis.morganborker.entity;

import javax.persistence.*;

@Entity
@Table(name = "market")
public class Market {
    @Id
    @GeneratedValue
    @Column(nullable = false, name = "market_id")
    private Long marketID;

    @Column(nullable = false, name = "future_id")
    private Long futureID;

    @Column(nullable = false, name = "buy_order_list_id")
    private Long buyOrderListID;

    @Column(nullable = false, name = "sell_order_list_id")
    private Long sellOrderListID;

    public Market() {
    }
}
