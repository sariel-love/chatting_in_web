package com.example.chatting_in_web.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleJsonError(HttpMessageNotReadableException e) {
        // 打印完整报错栈，控制台会显示「具体哪个字段」解析失败
        e.printStackTrace();
        // 返回友好提示，同时告诉你真实原因
        return "JSON解析失败：" + e.getMostSpecificCause().getMessage();
    }
}
