package com.back.cinetalk.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailDTO {

    private String posterImg;

    private String backGroundImg;

    private String title;

    private List<GenreDTO> genreDTOList;

    private String overview;

    private List<CastDTO> castDTOList;     // 출연진

    private List<CrewDTO> crewDTOList;     // 제작진

    private String releaseDate;     // 개봉날짜

    private String status;          // 상영 여부

    private String contentRating;   // 연령 등급

    private double tmdbScore;       // 평점 (tmdb)

    private double cinetalkScore;   // 평점 (cinetalk)

    private int runningTime;

    private List<ImageDTO> imageDTOList;

    private List<String> videoList;
}
