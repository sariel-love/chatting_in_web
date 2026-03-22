package com.example.chatting_in_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ToAiChat")
public class AIChatController {

    @RequestMapping("/AiChat")
    public static String AiChat(){return "aichat";}
}
