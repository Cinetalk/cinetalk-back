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
public class HoxyDTO {

    private Long movieId;
    private String movienm;
    private String poster_path;
    private int release_date;
    private List<Map<String,Object>> genres;

}
