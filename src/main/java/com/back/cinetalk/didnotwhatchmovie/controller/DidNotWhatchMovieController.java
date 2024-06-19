package com.back.cinetalk.didnotwhatchmovie.controller;

import com.back.cinetalk.didnotwhatchmovie.service.DidNotWhatchMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

public class DidNotWhatchMovieController {
    @RestController
    @RequestMapping("/movies")
    public class MovieController {
        @Autowired
        private DidNotWhatchMovieService didNotWhatchMovieService;

        @GetMapping("/unreviewed")
        public List<Map<String, Object>> getUnreviewedMovies(@RequestParam Long userId) {
            return didNotWhatchMovieService.getUnreviewedMovies(userId);
        }
    }
}
