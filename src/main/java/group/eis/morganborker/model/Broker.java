package group.eis.morganborker.model;

import java.util.Map;

public class Broker {
    private String brokerID;
    private String brokerName;
    private String brokerHttp;
    private String brokerWs;
    private String brokerToken;
    private Map<String, Market> markets;
}
