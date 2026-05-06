package com.example.chatting_in_web.service;

import com.example.chatting_in_web.dao.ChatDao;
import com.example.chatting_in_web.dao.UserDao;
import com.example.chatting_in_web.entity.ChatMessage;
import com.example.chatting_in_web.util.ChatRedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    ChatDao chatDao;

    @Autowired
    ChatRedisUtil chatRedisUtil;

    public void MessageSaveToRedis(ChatMessage message){
        String roomId = message.getGroup_id().toString();
        message.setIsSave(0);
        try {
            chatRedisUtil.pushMsgToRedis("room_"+roomId,message);
        }catch (Exception e){
            e.printStackTrace();
        }

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
