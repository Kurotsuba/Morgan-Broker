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

    public Long getFutureID() {
        return futureID;
    }

    public void setFutureID(Long futureID) {
        this.futureID = futureID;
    }

    public String getFutureName() {
        return futureName;
    }

    public void setFutureName(String futureName) {
        this.futureName = futureName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
