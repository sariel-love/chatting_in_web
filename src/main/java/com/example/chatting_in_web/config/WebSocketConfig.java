package com.example.chatting_in_web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Component
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private Chat chat;

    @Autowired
    private ChatHandShake chatHandShake;

    @Autowired
    private AiChatHandShake aiChatHandShake;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chat,"/websocket").addInterceptors(chatHandShake);
        registry.addHandler(chat,"/aiChat").addInterceptors(aiChatHandShake);
    }
}
