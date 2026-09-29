package com.example.springai.chat.service;

import com.example.springai.chat.model.Answer;
import com.example.springai.chat.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {

    private final ChatModel chatModel;

    @Value("${spring.ai.openai.chat.model}")
    private String modelName;

    @Override
    public Answer getAnswer(Question question) {
        System.out.println("현새 사용하는 chatModel 구현체=" + chatModel.getClass().getName());
        System.out.println("modelName=" +  modelName);

        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();

        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }
}
