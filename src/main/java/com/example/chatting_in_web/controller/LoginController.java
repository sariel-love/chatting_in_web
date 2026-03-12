package com.example.chatting_in_web.controller;

import com.example.chatting_in_web.entity.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.chatting_in_web.service.UserService;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    @ResponseBody
    public String Login(LoginUser loginUser, HttpServletRequest request){
//        LoginUser user = userService.FindUser_forLogin(loginUser.getPhone_number(),loginUser.getPassword());
//        if(user != null){//设置session为后续聊天业务做准备
            HttpSession session = request.getSession();
            session.setAttribute("loginUser",loginUser);
            return "{\"code\":\"200\",\"message\":\"success\"}";
//        }else{
//            return"{\"code\":500\",\"message\":\"false\"}";
//        }
    }
    @RequestMapping("/chat")
    public String  toChat(){
        return "chat";
    }
}
