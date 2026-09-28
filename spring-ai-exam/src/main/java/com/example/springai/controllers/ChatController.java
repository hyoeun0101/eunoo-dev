package com.example.springai.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
public class ChatController {
    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .call() // 동기. 전체 생성이 끝나야 응답. 긴 답변일수록 대기시간이 김.
                .content();
    }

    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .stream()// 토큰이 생성되는대로 토큰 단위로 응답
                .content();
    }




}

