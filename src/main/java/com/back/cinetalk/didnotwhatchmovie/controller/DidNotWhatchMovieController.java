package com.back.cinetalk.didnotwhatchmovie.controller;

import com.back.cinetalk.didnotwhatchmovie.dto.MovieRecommendationDTO;
import com.back.cinetalk.didnotwhatchmovie.entity.DidNotWhatchMovieEntity;
import com.back.cinetalk.didnotwhatchmovie.service.DidNotWhatchMovieService;
import com.back.cinetalk.movie.entity.MovieEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/main")
public class DidNotWhatchMovieController {
    private final DidNotWhatchMovieService service;

    @Autowired
    public DidNotWhatchMovieController(DidNotWhatchMovieService service) {
        this.service = service;
    }

    @PostMapping
    public DidNotWhatchMovieEntity createReview(@RequestBody DidNotWhatchMovieEntity entity) {
        return service.saveReview(entity);
    }

    @GetMapping("/recommend/{userId}")
    public List<MovieEntity> recommendMovies(@PathVariable Long userId) {
        List<MovieEntity> recommendedMovies = service.recommendMoviesByGenres(userId);

        return null;
    }
}
