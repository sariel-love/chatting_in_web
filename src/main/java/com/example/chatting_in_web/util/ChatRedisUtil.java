package com.example.chatting_in_web.util;

import com.example.chatting_in_web.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;
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
        String key = CHAT_MSG_PREFIX + roomId;
        // 左进右出，存入消息
        redisTemplate.opsForList().rightPush(key, msg);
        // 设置7天过期
        redisTemplate.expire(key, Duration.ofDays(EXPIRE_DAY));
    }

    /**
     * 获取该房间所有未同步入库的消息
     */
    public List<Object> getRoomAllMsg(String roomId) {
        String key = CHAT_MSG_PREFIX + roomId;
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        // 0到-1 取出全部
        return listOps.range(key, 0, -1);
    }
}
