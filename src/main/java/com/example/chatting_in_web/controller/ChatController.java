package com.example.chatting_in_web.controller;

import com.example.chatting_in_web.entity.ChatMessage;
import com.example.chatting_in_web.service.ChatService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.transform.Result;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatController {

    @RequestMapping("/chat")
    public String toChat() {
        log.info("跳转到群聊");
        return "chat";
    }

    @RequestMapping("/aichat")
    public String toAiChat() {
        log.info("跳转到与ai聊天");
        return "aichat";
    }


    @Autowired
    ChatService chatService;



    @RequestMapping("/messageSave")
    public ResponseEntity<?> saveMessage(@RequestBody List<ChatMessage> messages) {
        for (ChatMessage msg : messages) {
            System.out.println("收到消息：" + msg.getContent());
            chatService.MessageSave(msg);
        }
        return ResponseEntity.ok("消息保存成功");
    }

}
