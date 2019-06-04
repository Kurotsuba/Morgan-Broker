package group.eis.morganborker.service;

import org.springframework.stereotype.Component;


import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {
    private Session session;
    private String sid = "";
    private static CopyOnWriteArraySet<WebSocketServer> webSocketServers = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid){
        this.session = session;
        this.sid = sid;
        webSocketServers.add(this);
        try{
            sendMessage("Connected");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(){
        try {
            sendMessage("Connection Closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
        webSocketServers.remove(this);
    }

    @OnMessage
    public void onMessage(Session session, String message){
        System.out.println(message);
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}

