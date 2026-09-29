package com.example.springai.chat.service;

import com.example.springai.chat.model.Movie;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {
    private final ChatClient chatClient;

    public MovieServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public Movie findMovie(String title) {
        return chatClient.prompt()
                .user(u ->
                        u.text("영화 '{title}'의 제목, 개봉연도, 감독을 알려줘.")
                        .param("title", title))
                .call()
                .entity(Movie.class);
    }

    @Override
    public List<Movie> findMoviesByDirector(String director, int count) {
        return chatClient.prompt()
                .user(u -> u.text("{director} 감독의 대표작 {count}편을 알려줘.")
                        .param("director", director)
                        .param("count", String.valueOf(count)))
                .call()
                .entity(new ParameterizedTypeReference<List<Movie>>() {});
    }

    /**
     * 문자열로 JSON을 직접 요청하는 방식
     * 응답할 때 마크다운 포맷으로 응답하는 경우가 있음.
     * ```json {"title": "기생충"}```
     */
    @Override
    public String askRawJsonTrap(String title) {
        String raw = chatClient.prompt()
                .user(u -> u.text("영화 '{title}'의 정보를 title, year, director 필드를 가진 JSON으로 알려줘.")
                        .param("title", title))
                .call()
                .content();

        log.info("TRAP RAW >>> {}" , raw);

        return raw;
    }

    /**
     * 프롬프트에 마크다운 금지 명치
     */
    @Override
    public String askRawJsonFixed(String title) {
        String raw = chatClient.prompt()
                .user(u -> u.text("영화 '{title}'의 정보를 title, year, director 필드를 가진 JSON으로 알려줘."
                                + "Reponse in JSON format without markdown tags.")
                        .param("title", title))
                .call()
                .content();

        log.info("FIXED RAW>>>> {}", raw);

        return raw;
    }

    @Override
    public String describeFormat() {
        return "";
    }
}
