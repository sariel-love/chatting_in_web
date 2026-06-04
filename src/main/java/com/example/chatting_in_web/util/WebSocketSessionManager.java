package com.example.chatting_in_web.util;

import com.example.chatting_in_web.entity.ChatMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSocketSession 管理器
 * - 在本地保存 userId -> WebSocketSession
 * - 在 Redis RMap("ws:user-instance") 中写入 userId -> instanceId（用于跨实例路由）
 * - 提供 sendToLocalSession(userId, payload) 将消息发送给本地 session（如果在线）
 * - 提供 getResponseTopicName() 返回本实例订阅/发布的响应 topic（例如 ai:responses:{instanceId}）
 */
@Component
@Slf4j
public class WebSocketSessionManager {

    private final RedissonClient redissonClient;
    private final Map<String, WebSocketSession> localSessions = new ConcurrentHashMap<>(); // userId -> session
    private final RMap<String, String> userInstanceMap;
    private final String instanceId;
    private final String responseTopicPrefix;

    public WebSocketSessionManager(RedissonClient redissonClient,
                                   @Value("${ai.response.topic-prefix:ai:responses:}") String responseTopicPrefix) {
        this.redissonClient = redissonClient;
        this.instanceId = UUID.randomUUID().toString();
        this.userInstanceMap = redissonClient.getMap("ws:user-instance");
        this.responseTopicPrefix = responseTopicPrefix == null ? "ai:responses:" : responseTopicPrefix;
    }

    @PostConstruct
    public void init() {
        log.info("WebSocketSessionManager init, instanceId={}", instanceId);
    }

    @PreDestroy
    public void destroy() {
        log.info("WebSocketSessionManager destroy, instanceId={}", instanceId);
        // 可在这里做额外清理，例如移除当前实例在 userInstanceMap 中的映射（按需）
    }

    /**
     * 注册本地 session，并在 Redis 写入 user -> instanceId 映射
     * @param userId 用户唯一标识（例如 username）
     * @param session WebSocketSession
     */
    public void registerSession(String userId, WebSocketSession session) {
        System.out.println(session+"1111111111111");
        if (userId == null || session == null) return;
        System.out.println(userId);
        localSessions.put(userId, session);
        WebSocketSession session_ = localSessions.get(userId);
        System.out.println(session_+"!!!!!!!!!!!!!");
        try {
            userInstanceMap.put(userId, instanceId);

        } catch (Exception ex) {
            log.warn("registerSession: 写 Redis userInstanceMap 失败 userId={}, ex={}", userId, ex.getMessage());
        }
        log.info("registerSession userId={} on instance={}", userId, instanceId);
    }

    /**
     * 注销本地 session，并在 Redis 中删除映射（仅当映射指向当前实例）
     * @param userId
     */
    public void unregisterSession(String userId) {
        if (userId == null) return;
//        localSessions.remove(userId);
        try {
            String current = userInstanceMap.get(userId);
            if (instanceId.equals(current)) {
                userInstanceMap.remove(userId);
            }
        } catch (Exception ex) {
            log.warn("unregisterSession: 修改 Redis userInstanceMap 失败 userId={}, ex={}", userId, ex.getMessage());
        }
        log.info("unregisterSession userId={} from instance={}", userId, instanceId);
    }

    /**
     * 向本地 session 发送消息（线程可能来自 Redisson 的订阅回调线程或消费者线程）
     * 在发送前会检查 session 是否存在且打开
     * @param userId
     * @param payload 符合前端期望的 JSON 字符串或文本
     */
    public void sendToLocalSession(String userId, String payload) {
        System.out.println("userId=" + userId);
        System.out.println(payload);
        if (userId == null || payload == null) return;
        WebSocketSession session = localSessions.get(userId);
        System.out.println(session);
        if (session == null) {
            System.out.println("session is null");
            log.debug("sendToLocalSession: session not found for userId={}", userId);
            return;
        }
        if (!session.isOpen()) {
//            localSessions.remove(userId);
            log.debug("sendToLocalSession: session closed for userId={}", userId);
//            return;
        }
        System.out.println(payload+"2222222222222222");
        try {
            JSONObject jsonObject = new JSONObject(payload);
            System.out.println(payload+"MMMMMMMMMMMMMMMMMMMM");
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject firstChoice = choices.getJSONObject(0);
            String content = firstChoice.getJSONObject("message").getString("content");
            System.out.println("ai说：" + content);
            ChatMessage msg = new ChatMessage();
            msg.setContent(content);
            msg.setUsername("deepseek");
            msg.setGroup_id(1);
            System.out.println(msg);
            session.sendMessage(new TextMessage(GsonUtil.toJsonStringIgnoreNull(msg)));
            log.debug("sendToLocalSession: sent message to userId={}", userId);
        } catch (IOException e) {
            log.error("sendToLocalSession: send message failed for userId={}, ex={}", userId, e.getMessage());
        }
    }

    /**
     * 返回当前实例的响应 topic 名称，例如 ai:responses:{instanceId}
     */
    public String getResponseTopicName() {
        return responseTopicPrefix + instanceId;
    }

}