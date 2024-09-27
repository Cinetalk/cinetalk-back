package com.back.cinetalk.find.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindMovieDTO {

    private Long id;
    private String title;
    private String overview;
    private String poster_path;
}
