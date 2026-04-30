package com.example.chatting_in_web.config;

import com.example.chatting_in_web.entity.ChatMessage;
import com.example.chatting_in_web.entity.LoginUser;
import com.example.chatting_in_web.entity.Message;
import com.example.chatting_in_web.service.AiService;
import com.example.chatting_in_web.service.ChatService;
import com.example.chatting_in_web.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class Chat implements WebSocketHandler {

    public static final Map<String,WebSocketSession> USERS = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LoginUser loginUser = (LoginUser) session.getAttributes().get("loginUser");
        if(loginUser != null) {
            USERS.put(loginUser.getUsername(), session);
            log.info("用户{}{}加入在线用户列表",loginUser.getUsername(),loginUser.getPhone_number());
        }
//        if(session.getAttributes().get("AiUser") != null){
//            USERS.put("AiUser",session);
//            log.info("ai加入在线用户列表");
//        }
    }

    
    @Autowired
    AiService aiService;
    
    @Autowired
    ChatService chatService;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message.getPayloadLength() == 0) {
            return;
        }
        ChatMessage msg = GsonUtil.fromJson(message.getPayload().toString(), ChatMessage.class);//因发送方的发送的数据二进制的码，需要将二进制的码转化成字符串
        System.out.println(msg);
        LocalDateTime now = LocalDateTime.now();
        msg.setCreate_time(now);
        System.out.println(msg);
        chatService.MessageSave(msg);
        log.info("用户{}发送了消息：{}",msg.getUsername(),msg.getContent());
        if(msg.getGroup_id() == 1) {
                String data = aiService.AiChat(msg.getContent());
                System.out.println(data);
                JSONObject jsonObject = new JSONObject(data);
                JSONArray choices = jsonObject.getJSONArray("choices");
                JSONObject firstChoice = choices.getJSONObject(0);
                String content = firstChoice.getJSONObject("message").getString("content");
                System.out.println("ai说：" + content);
                msg.setContent(content);
                msg.setUsername("deepseek");
                msg.setGroup_id(1);
                System.out.println(msg);
                chatService.MessageSave(msg);
                sendMessageToUser(session, new TextMessage(GsonUtil.toJsonStringIgnoreNull(msg)));
            }else if(msg.getGroup_id() == 2)
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
