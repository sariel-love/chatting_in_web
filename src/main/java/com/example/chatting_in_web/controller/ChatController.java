package com.example.chatting_in_web.controller;

import com.example.chatting_in_web.entity.ChatMessage;
import com.example.chatting_in_web.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String messageSave(List<ChatMessage> list) {
        chatService.MessageSave(list);
        return "操作成功";
    }

}
