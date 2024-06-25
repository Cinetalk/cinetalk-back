package com.back.cinetalk.didnotwhatchmovie.dto;

import com.back.cinetalk.movie.entity.MovieEntity;
import com.back.cinetalk.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DidNotWhatchMovieDTO {

    private UserEntity user;
    private MovieEntity movie;

}
