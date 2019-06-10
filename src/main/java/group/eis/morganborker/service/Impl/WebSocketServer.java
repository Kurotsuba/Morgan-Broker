package group.eis.morganborker.service.Impl;

import group.eis.morganborker.utils.CopyOnWriteMap;
import org.springframework.stereotype.Component;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/websocket/{sid}")
@Component
public class WebSocketServer {
    private Session session;
    private String sid;
    private static CopyOnWriteMap<String, WebSocketServer> webSocketServers = new CopyOnWriteMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid){
        this.session = session;
        this.sid = sid;
        webSocketServers.put(sid, this);
        try{
            sendMessage("Connected");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(){
        webSocketServers.remove(this);
    }

    @OnMessage
    public void onMessage(Session session, String message){
        System.out.println(message);
    }

    @OnError
    public void onError(Session session, Throwable error){
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendInfo(String message) throws IOException{
        for(String key : webSocketServers.keySet()){
            webSocketServers.get(key).session.getBasicRemote().sendText(message);
        }
    }
}

