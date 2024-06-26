package com.back.cinetalk.didnotwhatchmovie.controller;

import com.back.cinetalk.didnotwhatchmovie.service.DidNotWatchMovieService;
import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.user.jwt.JwtValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DidNotWatchMovieController {
    private DidNotWatchMovieService didNotWatchMovieService;

    @GetMapping("/recommend")
    public List<MovieEntity> getRecommend(@JwtValidation String email) {
        return didNotWatchMovieService.getRecommendMovieList(email);
    }
}
