package com.example.chatting_in_web.service;

import com.alibaba.fastjson.JSON;
import com.example.chatting_in_web.dao.ChatDao;
import com.example.chatting_in_web.dao.UserDao;
import com.example.chatting_in_web.entity.ChatMessage;
import com.example.chatting_in_web.util.ChatRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    ChatDao chatDao;

    @Autowired
    ChatRedisUtil chatRedisUtil;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void MessageSaveToRedis(ChatMessage message){
        String roomId = message.getGroup_id().toString();
        message.setIsSave(0);
        try {
            chatRedisUtil.pushMsgToRedis("room_"+roomId,message);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public List<ChatMessage> getMessageFromRedis(String roomId){
        String key = "room_"+roomId;
        List<String> jsonList = stringRedisTemplate.opsForList().range(key,0,-1);
        if(jsonList.isEmpty()||jsonList == null ){
            return new ArrayList<>();
        }
        return jsonList.stream().map(str -> JSON.parseObject(str,ChatMessage.class))
                .collect(Collectors.toList());
    }

    public void MessageSaveToDB(){
        List<String> roomList = List.of("room_1", "room_2");

        for (String roomId : roomList) {
            List<ChatMessage> unSyncMsgList = chatRedisUtil.getUnSyncMsg(roomId);

            if (unSyncMsgList.isEmpty()) {
                continue;
            }

            try {
                chatDao.batchInsert(unSyncMsgList);
                chatRedisUtil.markMsgAsSynced(roomId, unSyncMsgList);

                System.out.println("房间：" + roomId + " 同步成功：" + unSyncMsgList.size() + "条");
            } catch (Exception e) {
                System.out.println("同步失败：" + e.getMessage());
                System.out.println(e);
            }
        }
    }
    public List<ChatMessage> GetMessage(int group_id){
        return chatDao.getDB(group_id);
    }



}
