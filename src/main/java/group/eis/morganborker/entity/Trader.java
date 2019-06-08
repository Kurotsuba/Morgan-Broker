package group.eis.morganborker.entity;


import javax.persistence.*;

@Entity
@Table(name = "trader")
public class Trader {
    @Id
    @Column(nullable = false, name = "trader_id")
    private Long traderID;

    @Column(nullable = false, name = "trader_name")
    private String traderName;


    public Trader() {
    }

    public Trader(Long traderID, String traderName) {
        this.traderID = traderID;
        this.traderName = traderName;
    }

    public Long getTraderID() {
        return traderID;
    }

    public void setTraderID(Long traderID) {
        this.traderID = traderID;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

}
