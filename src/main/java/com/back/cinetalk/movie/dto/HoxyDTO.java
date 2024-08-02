package com.back.cinetalk.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HoxyDTO {

    private Long movieId;
    private String movienm;
    private String poster_path;

}
