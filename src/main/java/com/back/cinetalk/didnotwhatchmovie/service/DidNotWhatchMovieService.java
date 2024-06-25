package com.back.cinetalk.didnotwhatchmovie.service;

import com.back.cinetalk.didnotwhatchmovie.dto.DidNotWhatchMovieDTO;
import com.back.cinetalk.didnotwhatchmovie.entity.DidNotWhatchMovieEntity;
import com.back.cinetalk.didnotwhatchmovie.entity.MovieGenreEntity;
import com.back.cinetalk.didnotwhatchmovie.repository.DidNotWhatchMovieRepository;
import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.movie.repository.MovieRepository;
import com.back.cinetalk.movie.service.CallAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DidNotWhatchMovieService {

    @Value("${tmdb.rest.api.key}")
    private String tmdbrestapikey;
    private final DidNotWhatchMovieRepository repository;
    private final MovieRepository movieRepository;

    public DidNotWhatchMovieEntity saveReview(DidNotWhatchMovieDTO dto) {
        Optional<DidNotWhatchMovieEntity> existingReview = repository.findByUserIdAndMovieId(dto.getUser().getId(), dto.getMovie().getId());
        if (existingReview.isPresent()) {
            throw new IllegalStateException("User has already reviewed this movie.");
        }
        DidNotWhatchMovieEntity entity = DidNotWhatchMovieEntity.builder()
                .user(dto.getUser())
                .movie(dto.getMovie())
                .build();
        return repository.save(entity);
    }

    public List<MovieEntity> recommendMoviesByGenres(Long userId) {
        List<DidNotWhatchMovieEntity> userReviews = repository.findByUserId(userId);
        Map<Long, Long> genreCount = new HashMap<>();

        for (DidNotWhatchMovieEntity review : userReviews) {
            for (MovieGenreEntity movieGenre : review.getMovie().getGenres()) {
                genreCount.merge(movieGenre.getGenre().getId(), 1L, Long::sum);
            }
        }

        List<Long> popularGenreIds = genreCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (popularGenreIds.isEmpty()) {
            return Collections.emptyList();
        }

        return movieRepository.findByGenreIds(popularGenreIds);
    }
}
