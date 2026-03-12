package com.example.chatting_in_web.entity;

public class LoginUser {
    private String phone_number;
    private String password;

    @Override
    public String toString() {
        return "LoginUser{" +
                "phone_number='" + phone_number + '\'' +
                ", password='" + password + '\'' +
                '}';
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
