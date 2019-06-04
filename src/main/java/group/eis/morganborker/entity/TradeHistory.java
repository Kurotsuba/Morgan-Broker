package group.eis.morganborker.entity;

import javax.persistence.*;

@Entity
@Table(name = "trade_history")
public class TradeHistory {
    @Id
    @GeneratedValue
    @Column(nullable = false, name = "trade_id")
    private Long tradeID;

    @Column(nullable = false, name = "trader_id")
    private Long traderID;

    private
}
