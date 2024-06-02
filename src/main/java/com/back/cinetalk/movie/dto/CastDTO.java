package com.back.cinetalk.movie.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CastDTO {

    private int id;

    private String name;

    private String character;

    private int order;

    private String profilePath;
}
