package com.example.springai.chat.controller;

import com.example.springai.chat.model.Movie;
import com.example.springai.chat.service.MovieService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // 영화 title의 정보를 알려줘.
    @GetMapping("/one")
    public Movie one(@RequestParam(defaultValue =  "기생충") String title) {
        return movieService.findMovie(title);
    }

    @GetMapping("/by-director")
    public List<Movie> byDirector(@RequestParam(defaultValue = "봉준호") String director,
                                  @RequestParam(defaultValue = "3") int count) {
        return movieService.findMoviesByDirector(director, count);
    }

    @GetMapping("/raw-json-trap")
    public String rawJsonTrap(@RequestParam(defaultValue =  "기생충") String title) {
        return movieService.askRawJsonTrap(title);
    }

    @GetMapping("/raw-json-fixed")
    public String rawJsonFixed(@RequestParam(defaultValue =  "기생충") String title) {
        return movieService.askRawJsonFixed(title);
    }
}
