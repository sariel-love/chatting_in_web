package com.example.chatting_in_web.controller;

import com.example.chatting_in_web.entity.LoginUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.chatting_in_web.service.UserService;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    public String Login(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response){
        String result = userService.FindUser_forLogin(loginUser.getPhone_number(),loginUser.getPassword());
        String username = userService.FindUserName(loginUser.getPhone_number());
        loginUser.setUsername(username);
        System.out.println(result);
        if(result.equals("welcome logining")){//设置session为后续聊天业务做准备
            HttpSession session = request.getSession();
            session.setAttribute("loginUser",loginUser);
            if(username != null){
                Cookie cookie = new Cookie("username",loginUser.getUsername());
                cookie.setMaxAge(60*60*24);
                cookie.setPath("/");
                response.addCookie(cookie);
            }else{
                System.out.println("cookie has an error");
            }
            return "{\"code\":\"200\",\"message\":\"success\"}";
       }else if(result.equals("Password is not true")){
           return "{\"code\":400\",\"message\":\"false\"}";
        }else{
            return "{\"code\":500\",\"message\":\"false\"}";
        }

    }
    @RequestMapping("/chat")
    public String  toChat(){
        return "chat";
    }

    @RequestMapping("/aichat")
    public String  toAiChat(){
        return "aichat";
    }

    @RequestMapping("/testing")
    public String  toTesting(){
        return "testing";
    }

    @RequestMapping("/register")
    public String  toRegister(){
        return "register";
    }
}
