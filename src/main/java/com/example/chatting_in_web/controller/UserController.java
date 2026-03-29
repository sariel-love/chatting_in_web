package com.example.chatting_in_web.controller;

import com.example.chatting_in_web.entity.User;
import com.example.chatting_in_web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/toRegister")
    @ResponseBody
    public String Add_User(User user){
        int addUser = userService.Add_User(user);
        if(addUser == 0){
            return "{\"code\":\"500\",\"message\":\"false\"}";
        }else if(addUser == 2){
            return "{\"code\":\"400\",\"message\":\"hava exited\"}";
        }
        return "{\"code\":\"200\",\"message\":\"success\"}";
    }

}
