package com.example.chatting_in_web.dao;

import com.example.chatting_in_web.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatDao {
    public void SaveMessage(@Param("chatMessages")List<ChatMessage> chatMessages);
}
