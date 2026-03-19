package com.example.chatting_in_web.service;

import com.example.chatting_in_web.dao.UserDao;
import com.example.chatting_in_web.entity.LoginUser;
import com.example.chatting_in_web.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public String FindUser_forLogin(String phone_number,String password){
        List<LoginUser> User_forLogin = userDao.findUser(phone_number);
        if(User_forLogin == null){
            return "This user is not existing";
        }
        if(!password.equals( User_forLogin.get(0).getPassword())){
            return "Password is not true";
        }
        return "welcome logining";
    }

    public String FindUserName(String phone_number){
        String username = userDao.findUsername(phone_number);
        if(username != null){
            return username;
        }
        return "000";
    }

}
