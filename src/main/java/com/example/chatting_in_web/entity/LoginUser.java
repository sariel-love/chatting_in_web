package com.example.chatting_in_web.entity;

public class LoginUser {
    private String username;
    private String phone_number;
    private String password;

    @Override
    public String toString() {
        return "LoginUser{" +
                "username='" + username + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
