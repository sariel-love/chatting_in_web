package com.example.chatting_in_web.config;

import com.example.chatting_in_web.entity.LoginUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class AiChatHandShake implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest rep = (ServletServerHttpRequest) request;
        HttpSession session = rep.getServletRequest().getSession();
        if(session.getAttribute("LoginUser") != null){
            LoginUser AiUser = new LoginUser();
            AiUser.setUsername("ai");
            attributes.put("AiUser",AiUser);
            attributes.put("LoginUser",session.getAttribute("LoginUser"));
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
