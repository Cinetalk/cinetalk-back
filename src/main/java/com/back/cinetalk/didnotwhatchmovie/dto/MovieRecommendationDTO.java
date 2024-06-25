package com.back.cinetalk.didnotwhatchmovie.dto;

import com.back.cinetalk.movie.entity.MovieEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieRecommendationDTO {

    private Long userId;

    private List<MovieEntity> recommendedMovies;

}
