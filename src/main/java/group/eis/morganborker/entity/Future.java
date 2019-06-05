package group.eis.morganborker.entity;

import javax.persistence.*;

@Entity
@Table(name = "future")
public class Future {
    @Id
    @GeneratedValue
    @Column(nullable = false, name = "future_id")
    private Long futureID;

    @Column(nullable = false, name = "future_name")
    private String futureName;

    @Column(nullable = false, name = "period")
    private String period;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "category")
    private String category;

    @Column(name = "expired")
    private String expired;

    @Column(name = "icon")
    private String icon;

    public Future() {
    }
}
