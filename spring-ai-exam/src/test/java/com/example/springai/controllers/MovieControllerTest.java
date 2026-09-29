package com.example.springai.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.example.springai.chat.controller.MovieController;
import com.example.springai.chat.model.Movie;
import com.example.springai.chat.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@WebMvcTest(MovieController.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MovieControllerTest {
    final MockMvcTester mvcTester;

    @MockitoBean
    MovieService movieService;

    @Test
    void findMovieInfo() {
        given(movieService.findMovie("기생충")).willReturn(new Movie("기생충", 2019, "봉준호"));

        MvcTestResult result = mvcTester.get().uri("/api/movies/one")
                .param("title", "기생충")
                .exchange();

        assertThat(result).hasStatusOk()
                .bodyJson().extractingPath("$.title").isEqualTo("기생충");
    }

}
