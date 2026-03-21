package com.example.chatting_in_web.util;

import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class AiUtil {

    public static void AIChat() throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"messages\": [\n    {\n      \"content\": \"You are a helpful assistant\",\n      \"role\": \"system\"\n    },\n    {\n      \"content\": \"Hi\",\n      \"role\": \"user\"\n    }\n  ],\n  \"model\": \"deepseek-chat\",\n  \"thinking\": {\n    \"type\": \"disabled\"\n  },\n  \"frequency_penalty\": 0,\n  \"max_tokens\": 4096,\n  \"presence_penalty\": 0,\n  \"response_format\": {\n    \"type\": \"text\"\n  },\n  \"stop\": null,\n  \"stream\": false,\n  \"stream_options\": null,\n  \"temperature\": 1,\n  \"top_p\": 1,\n  \"tools\": null,\n  \"tool_choice\": \"none\",\n  \"logprobs\": false,\n  \"top_logprobs\": null\n}");
        Request request = new Request.Builder()
                .url("https://api.deepseek.com/chat/completions")   //请求的api端口
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer sk-d84d57ff3622478fbf91dd06f5d6f569")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }


    public static void main(String[] args) throws IOException {
        AIChat();
    }
}
