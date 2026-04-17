package com.example.chatting_in_web.service;

import com.example.chatting_in_web.dao.ChatDao;
import com.example.chatting_in_web.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatDao chatDao;

    public void MessageSave(ChatMessage message){
        chatDao.SaveMessage(message);
    }
}
