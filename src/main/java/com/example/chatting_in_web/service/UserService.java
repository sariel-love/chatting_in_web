package com.example.chatting_in_web.service;

import com.example.chatting_in_web.dao.UserDao;
import com.example.chatting_in_web.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
     private UserDao userDao;
    public LoginUser FindUser_forLogin(String phone_number,String password){
        LoginUser User_forLogin = userDao.findUser(phone_number,password);
        return User_forLogin;
    }
}
