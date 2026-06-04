package com.example.chatting_in_web.entity;

public class AiRequest {
    private String userId;       // username
    private String content;      // 用户消息内容
    private String replyTopic;   // 目标实例的 topic（例如 ai:responses:{instanceId}）

    public AiRequest() {}

    public AiRequest(String userId, String content, String replyTopic) {
        this.userId = userId;
        this.content = content;
        this.replyTopic = replyTopic;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getReplyTopic() { return replyTopic; }
    public void setReplyTopic(String replyTopic) { this.replyTopic = replyTopic; }
}