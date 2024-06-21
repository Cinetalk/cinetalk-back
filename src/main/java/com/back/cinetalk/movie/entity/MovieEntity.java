package com.back.cinetalk.movie.entity;

import com.back.cinetalk.didnotwhatchmovie.entity.DidNotWhatchMovieEntity;
import com.back.cinetalk.didnotwhatchmovie.entity.MovieGenreEntity;
import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.movie.dto.MovieDTO;
import com.back.cinetalk.review.entity.ReviewEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "Movie")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MovieEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long movieId;

    private String movienm;

    private int audiAcc;


    public static MovieEntity ToMovieEntity(MovieDTO movieDTO) {
        return MovieEntity.builder()
                .id(movieDTO.getId())
                .movieId(movieDTO.getMovieId())
                .movienm(movieDTO.getMovienm())
                .audiAcc(movieDTO.getAudiAcc())
                .build();
    }

    // 승일

    @OneToMany(mappedBy = "movie")
    private List<DidNotWhatchMovieEntity> reviews;

    @Getter
    @OneToMany(mappedBy = "movie")
    private List<MovieGenreEntity> genres;

}

