package group.eis.morganborker.entity;

import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import java.util.Map;

@Entity
@Table(name = "broker")
public class Broker {
    @Id
    @GeneratedValue
    @Column(nullable = false, name = "broker_id")
    private Long brokerID;

    @Column(nullable = false, name = "broker_name")
    private String brokerName;

    @Column(name = "broker_password")
    private String brokerPassword;


    private Map<String, Market> markets;
}
