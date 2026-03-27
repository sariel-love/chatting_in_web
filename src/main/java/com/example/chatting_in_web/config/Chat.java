package com.example.chatting_in_web.config;

import com.example.chatting_in_web.entity.LoginUser;
import com.example.chatting_in_web.entity.Message;
import com.example.chatting_in_web.service.AiService;
import com.example.chatting_in_web.util.GsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
        if(loginUser != null) {
            USERS.put(loginUser.getUsername(), session);
        }
        if(session.getAttributes().get("AiUser") != null){
            USERS.put("AiUser",session);
        }
        System.out.println("MMMMMMMMMMMMMMM");
    }

    
    @Autowired
    AiService aiService;
    
    
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message.getPayloadLength() == 0) {
            return;
        }
        System.out.println("接收到消息：" + message.getPayload().toString());
        Message msg = GsonUtil.fromJson(message.getPayload().toString(), Message.class);//因发送方的发送的数据二进制的码，需要将二进制的码转化成字符串
            String data = aiService.AiChat(msg.getMessage());
            System.out.println(data);
            JSONObject jsonObject = new JSONObject(data);
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject firstChoice = choices.getJSONObject(0);
            String content = firstChoice.getJSONObject("message").getString("content");
            System.out.println("ai说：" + content);
            msg.setMessage(content);
            msg.setUsername("deepseek");
            sendMessageToUser(session, new TextMessage(GsonUtil.toJsonStringIgnoreNull(msg)));


//            sendMessageToAll(message);

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
    public void  sendMessageToUser(WebSocketSession session,WebSocketMessage<?> message) throws IOException{
        session.sendMessage(message);
    }



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
