package com.example.chatting_in_web.entity;

public class Message {
    private String message;
    private String toUser;
    private String username;

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", toUser='" + toUser + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
