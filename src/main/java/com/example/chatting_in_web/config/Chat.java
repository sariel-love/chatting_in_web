package com.example.chatting_in_web.config;

import com.example.chatting_in_web.entity.LoginUser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class Chat implements WebSocketHandler {

    public static final Map<String,WebSocketSession> USERS = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LoginUser loginUser = (LoginUser) session.getAttributes().get("loginUser");
        if (loginUser == null) {
            session.sendMessage(new TextMessage("用户未登录，无法发送消息"));
            return;
        }
        USERS.put(loginUser.getUsername(),session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if(message.getPayloadLength() == 0){
            return;
        }
        System.out.println("接收到消息："+message.getPayload().toString());

        sendMessageToAll(message);

    }
//群发
    public void sendMessageToAll( WebSocketMessage<?> message) throws IOException {
        for(Map.Entry<String,WebSocketSession> entry :USERS.entrySet()){
            WebSocketSession webSocketSession = entry.getValue();
            if(webSocketSession.isOpen()){
                webSocketSession.sendMessage(message);
            }
        }
    }
//私发




    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
