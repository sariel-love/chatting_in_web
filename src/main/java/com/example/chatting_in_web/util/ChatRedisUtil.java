package com.example.chatting_in_web.util;

import com.example.chatting_in_web.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChatRedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Redis Key 前缀：每个房间单独一个List
    private static final String CHAT_MSG_PREFIX = "chat:room:msg:";
    // 消息在Redis保留 7天
    private static final long EXPIRE_DAY = 7;

    /**
     * 保存聊天消息到Redis
     */
    public void pushMsgToRedis(String roomId, ChatMessage msg) {
        String key = roomId;
        // 左进右出，存入消息
        redisTemplate.opsForList().rightPush(key, msg);
        // 设置7天过期
        redisTemplate.expire(key, Duration.ofDays(EXPIRE_DAY));
    }

    /**
     * 获取该房间所有未同步入库的消息
     */
    public List<Object> getRoomAllMsg(String roomId) {
        String key =  roomId;
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        // 0到-1 取出全部
        return listOps.range(key, 0, -1);
    }


    public List<ChatMessage> getUnSyncMsg(String roomId) {
        String key =  roomId;
        List<Object> range = redisTemplate.opsForList().range(key, 0, -1);
        List<ChatMessage> unSyncList = new ArrayList<>();

        for (Object o : range) {
            if (o instanceof ChatMessage msg) {
                // 只收集未同步的消息
                if (msg.getIsSave() == null || msg.getIsSave() == 0) {
                    unSyncList.add(msg);
                }
            }
        }
        return unSyncList;
    }


    public void markMsgAsSynced(String roomId, List<ChatMessage> msgList) {
        String key = roomId;
        List<Object> range = redisTemplate.opsForList().range(key, 0, -1);
        if (range == null || range.isEmpty()) return;

        List<Object> newList = new ArrayList<>();
        for (Object o : range) {
            if (!(o instanceof ChatMessage oldMsg)) continue;

            // 如果是本次同步成功的消息，标记为已同步
            boolean needMark = msgList.stream()
                    .anyMatch(m -> m.getCreate_time().equals(oldMsg.getCreate_time())
                            && m.getUsername().equals(oldMsg.getUsername())
                            && m.getContent().equals(oldMsg.getContent()));

            if (needMark) {
                oldMsg.setIsSave(1);
            }
            newList.add(oldMsg);
        }

        // 覆盖原来的Redis列表
        redisTemplate.delete(key);
        if (!newList.isEmpty()) {
            redisTemplate.opsForList().rightPushAll(key, newList);
            redisTemplate.expire(key, Duration.ofDays(7));
        }
    }
}
