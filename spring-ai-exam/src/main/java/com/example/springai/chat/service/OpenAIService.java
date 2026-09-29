package com.example.springai.chat.service;


import com.example.springai.chat.model.Answer;
import com.example.springai.chat.model.Question;

public interface OpenAIService {

    Answer getAnswer(Question question);
}
