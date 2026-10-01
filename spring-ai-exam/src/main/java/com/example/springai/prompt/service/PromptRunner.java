package com.example.springai.prompt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

/**
 * 호출마다 토큰 사용량을 로그로 남긴다. - S16의 "시스템 프롬프트 길이=고정비"를 수치로 확인하기 위함.
 */
@Component
@Slf4j
public class PromptRunner {

}
