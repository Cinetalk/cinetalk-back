package com.back.cinetalk.didnotwhatchmovie.service;

import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.movie.repository.MovieRepository;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class DidNotWatchMovieService {

    private UserRepository userRepository;
    private ReviewRepository reviewRepository;
    private MovieRepository movieRepository;

    public List<MovieEntity> getRecommendMovieList(String email) {
        List<ReviewEntity> reviewEntiy = reviewRepository.findByUser_Email(email);
        List<String> genreList = new ArrayList<>();
        List<Long> reveiwedMovieIds = new ArrayList<>();
        for(ReviewEntity review: reviewEntiy) {
            Long movieId = review.getMovieId();
            MovieEntity movieEntity = movieRepository.findByMovieId(movieId);
            genreList.add(movieEntity.getGenre());
            reveiwedMovieIds.add(movieId);
        }
        String mostFrequentGenre = findMostFrequent(genreList);
        List<MovieEntity> movieEntities = movieRepository.findByGenre(mostFrequentGenre);
        List<MovieEntity> filteredMovies = filterMovies(movieEntities, reveiwedMovieIds);
        return filteredMovies;
    }

    public static List<MovieEntity> filterMovies(List<MovieEntity> movieList, List<Long> excludeMovieIds) {
        return movieList.stream()
                .filter(movie -> !excludeMovieIds.contains(movie.getMovieId()))
                .collect(Collectors.toList());
    }

    public static String findMostFrequent(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String item : list) {
            frequencyMap.put(item, frequencyMap.getOrDefault(item, 0) + 1);
        }

        String mostFrequent = null;
        int maxCount = -1;
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostFrequent = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return mostFrequent;
    }

}
