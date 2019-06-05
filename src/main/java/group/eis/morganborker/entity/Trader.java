package group.eis.morganborker.entity;


import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "trader")
public class Trader {
    @Id
    @Column(nullable = false, name = "trader_id")
    private Long traderID;

    @Column(nullable = false, name = "trader_name")
    private String traderName;

    @Column(name = "order_list_id")
    private Long[] orderList;

    public Trader() {
    }
}
