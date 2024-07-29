package com.back.cinetalk.movie.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class BannerDTO {

    private Long movieId;

    private String movienm;

    private String poster_path;

    private String backdrop_path;

    private List<Map<String,Object>> genres;

    private double rate;

    private String keyword;

    private List<BannerReviewDTO> ReviewList;
}