package com.example.chatting_in_web.service.impl;

import com.example.chatting_in_web.service.AiService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AiServiceImpl implements AiService {
    @Value("${ai.deepseek.api-key}")
    private String apiKey;
    @Value("${ai.deepseek.base-url}")
    private String baseUrl;
    @Value("${ai.deepseek.model:deepseek-chat}") // 冒号后面是默认值
    private String model;
    @Value("${ai.deepseek.temperature:0.7}")
    private double temperature;
    @Value("${ai.deepseek.top-p:0.8}")
    private double topP;

    public String AiChat(String message){
        String data = "";
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            String jsonBody = String.format("{\n"
                    + "  \"model\": \"%s\",\n"
                    + "  \"messages\": [\n"
                    + "    {\"role\": \"user\", \"content\": \"%s\"}\n"
                    + "  ],\n"
                    + "  \"temperature\": %f,\n"
                    + "  \"top_p\": %f\n"
                    + "}", model,message,temperature,topP);
            RequestBody body = RequestBody.create(mediaType,jsonBody);
            Request request = new Request.Builder()
                    .url(baseUrl)   //请求的api端口
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", apiKey)
                    .build();
            Response response = client.newCall(request).execute();
            data = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
