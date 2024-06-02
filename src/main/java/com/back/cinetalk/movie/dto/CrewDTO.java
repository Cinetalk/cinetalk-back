package com.back.cinetalk.movie.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CrewDTO {

    private int id;

    private String name;

    private String job;

    private String profilePath;
}
