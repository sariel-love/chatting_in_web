package com.example.chatting_in_web.controller;

import com.example.chatting_in_web.entity.LoginUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.chatting_in_web.service.UserService;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
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
        if(result.equals("welcome logining")){//设置session为后续聊天业务做准备
            log.info("{}登陆成功",username);
            HttpSession session = request.getSession();
            session.setAttribute("loginUser",loginUser);
            if(username != null){
                Cookie cookie = new Cookie("username",loginUser.getUsername());
                cookie.setMaxAge(60*60*24);
                cookie.setPath("/");
                response.addCookie(cookie);
            }else{
                log.info("cookie has an error");
            }

            return "{\"code\":\"200\",\"message\":\"success\"}";
       }else if(result.equals("Password is not true")){
            log.info("{}因密码错误无法登录",username);
           return "{\"code\":400\",\"message\":\"false\"}";
        }else{
            log.info("手机号为{}的用户未注册",loginUser.getPhone_number());
            return "{\"code\":500\",\"message\":\"false\"}";
        }

        }
    @RequestMapping("/chat")
    public String  toChat(){
        log.info("跳转到群聊");
        return "chat";
    }

    @RequestMapping("/aichat")
    public String  toAiChat(){
        log.info("跳转到与ai聊天");
        return "aichat";
    }


    @RequestMapping("/register")
    public String  toRegister(){
        log.info("跳转到注册页面");
        return "register";
    }
}
