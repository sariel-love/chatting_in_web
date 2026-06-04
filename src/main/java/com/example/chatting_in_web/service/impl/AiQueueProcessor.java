package com.example.chatting_in_web.service.impl;

import com.example.chatting_in_web.entity.AiRequest;
import com.example.chatting_in_web.service.AiService;
import com.example.chatting_in_web.util.WebSocketSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.*;

@Component
public class AiQueueProcessor {

    private final RedissonClient redissonClient;
    private final AiService aiService;
    private final WebSocketSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    private final String queueName;
    private final String lockPrefix;
    private final int lockWaitSeconds;
    private final int lockLeaseSeconds;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = true;

    public AiQueueProcessor(RedissonClient redissonClient,
                            AiService aiService,
                            WebSocketSessionManager sessionManager,
                            ObjectMapper objectMapper,
                            @Value("${ai.queue.name:ai:queue}") String queueName,
                            @Value("${ai.lock.prefix:ai:lock:user:}") String lockPrefix,
                            @Value("${ai.lock.wait-seconds:5}") int lockWaitSeconds,
                            @Value("${ai.lock.lease-seconds:300}") int lockLeaseSeconds) {
        this.redissonClient = redissonClient;
        this.aiService = aiService;
        this.sessionManager = sessionManager;
        this.objectMapper = objectMapper;
        this.queueName = queueName;
        this.lockPrefix = lockPrefix;
        this.lockWaitSeconds = lockWaitSeconds;
        this.lockLeaseSeconds = lockLeaseSeconds;
    }

    @PostConstruct
    public void start() {
        // 订阅本实例的响应 Topic
        String myTopicName = sessionManager.getResponseTopicName();
        RTopic subscribeTopic = redissonClient.getTopic(myTopicName);
        subscribeTopic.addListener(String.class, (channel, msg) -> {
            try {
                // msg 是我们发布的 JSON：{userId, aiResponse}
                ResponsePayload payload = objectMapper.readValue(msg, ResponsePayload.class);
                sessionManager.sendToLocalSession(payload.getUserId(), payload.getAiResponse());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 启动消费者线程
        executor.submit(() -> {
            RBlockingQueue<String> queue = redissonClient.getBlockingQueue(queueName);
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    String json = queue.take(); // 阻塞直到有消息
                    AiRequest req = objectMapper.readValue(json, AiRequest.class);

                    String userLockKey = lockPrefix + req.getUserId();
                    RLock lock = redissonClient.getLock(userLockKey);
                    boolean locked = false;
                    try {
                        locked = lock.tryLock(lockWaitSeconds, lockLeaseSeconds, TimeUnit.SECONDS);
                        if (!locked) {
                            // 获取锁失败：简单策略为重新入队尾部
                            queue.add(json);
                            continue;
                        }

                        // 调用 AI（在后台线程）
                        String aiResponse = aiService.AiChat(req.getContent());

                        // 构造响应负载并发布到目标实例 topic
                        System.out.println(aiResponse);
                        ResponsePayload resp = new ResponsePayload(req.getUserId(), aiResponse);
                        String payloadJson = objectMapper.writeValueAsString(resp);
                        RTopic topic = redissonClient.getTopic(req.getReplyTopic());
                        topic.publish(payloadJson);

                    } finally {
                        if (locked) {
                            try { lock.unlock(); } catch (Exception ignore) {}
                        }
                    }

                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @PreDestroy
    public void stop() {
        running = false;
        executor.shutdownNow();
    }

    // 响应负载类
    public static class ResponsePayload {
        private String userId;
        private String aiResponse;
        public ResponsePayload() {}
        public ResponsePayload(String userId, String aiResponse) {
            this.userId = userId;
            this.aiResponse = aiResponse;
        }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getAiResponse() { return aiResponse; }
        public void setAiResponse(String aiResponse) { this.aiResponse = aiResponse; }
    }
}