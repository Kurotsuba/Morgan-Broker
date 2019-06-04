package group.eis.morganborker.service;

import org.springframework.stereotype.Component;
import org.yeauty.annotation.OnClose;
import org.yeauty.annotation.OnOpen;
import org.yeauty.annotation.ServerEndpoint;
import org.yeauty.pojo.Session;

import javax.websocket.OnMessage;
import javax.websocket.server.PathParam;
import java.io.IOException;

@ServerEndpoint("/websocket")
@Component
public class WebSocketServer {
    private Session session;
    private String sid = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid){

    }

    @OnClose
    public void onClose(){

    }

    @OnMessage
    public void onMessage(Session session, String message){

    }

    public void sendMessage(String message) throws IOException {
        this.session.sendText(message);
    }
}

