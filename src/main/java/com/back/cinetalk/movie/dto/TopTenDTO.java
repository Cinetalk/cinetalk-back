package com.back.cinetalk.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopTenDTO {

    private Long movieId;

    private String movienm;

    private String poster_path;

    private String release_date;

    private List<Map<String,Object>> genres;

    private Double TMDBRate;

    private Double rate;

    private Long reviewCount;

    private List<TopTenReviewDTO> reviewList;
}
