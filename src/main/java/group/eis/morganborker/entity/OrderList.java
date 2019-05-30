package group.eis.morganborker.entity;

import javax.persistence.*;

@Entity
@Table(name = "order_list")
public class OrderList {
    @Id
    @GeneratedValue
    @Column(nullable = false, name = "order_list_id")
    private Long orderListID;

    @Column(name = "type", nullable = false)
    private char type;

    @Column(name = "orders")
    private Long[] orders;
}
