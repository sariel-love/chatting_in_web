package com.example.chatting_in_web.entity;

import java.time.LocalDateTime;

public class ChatMessage {
    private Integer id;
    private Integer group_id;
    private String username;
    private String content;
    private LocalDateTime create_time;

    @Override
    public String toString() {
        return "group_content{" +
                "id=" + id +
                ", group_id=" + group_id +
                ", username='" + username + '\'' +
                ", content='" + content + '\'' +
                ", create_time=" + create_time +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreate_time() {
        return create_time;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }
}
