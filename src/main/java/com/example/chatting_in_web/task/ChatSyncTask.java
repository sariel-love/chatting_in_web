package com.example.chatting_in_web.task;


import com.example.chatting_in_web.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatSyncTask {

    private final ChatService chatService;

    public ChatSyncTask(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @Scheduled(fixedRate = 30000)
    public void syncTask() {
        try {
           chatService.MessageSaveToDB();
       }catch (Exception e){
           log.info("定时同步失败");
       }
    }


}
