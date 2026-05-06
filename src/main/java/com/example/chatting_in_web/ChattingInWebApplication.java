package com.example.chatting_in_web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChattingInWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChattingInWebApplication.class, args);
    }

}
