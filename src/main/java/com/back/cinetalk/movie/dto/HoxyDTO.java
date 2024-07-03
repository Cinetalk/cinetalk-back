package com.back.cinetalk.movie.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class HoxyDTO {

    private Long movieId;
    private String movienm;
    private String poster_path;

    public HoxyDTO(Long movieId, String movienm, String poster_path) {
        this.movieId = movieId;
        this.movienm = movienm;
        this.poster_path = poster_path;
    }
}
