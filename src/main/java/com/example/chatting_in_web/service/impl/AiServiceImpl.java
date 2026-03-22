package com.example.chatting_in_web.service.impl;

import com.example.chatting_in_web.service.AiService;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AiServiceImpl implements AiService {
    public String AiChat(String message){
        String data = "";
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            String jsonBody = String.format("{\n"
                    + "  \"model\": \"deepseek-chat\",\n"
                    + "  \"messages\": [\n"
                    + "    {\"role\": \"user\", \"content\": \"%s\"}\n"
                    + "  ],\n"
                    + "  \"temperature\": 0.7,\n"
                    + "  \"top_p\": 0.8\n"
                    + "}", message);
            RequestBody body = RequestBody.create(mediaType,jsonBody);
            Request request = new Request.Builder()
                    .url("https://api.deepseek.com/chat/completions")   //请求的api端口
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Bearer ")
                    .build();
            Response response = client.newCall(request).execute();
            data = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
