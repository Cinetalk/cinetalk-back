package com.back.cinetalk.movie.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.movie.dto.MovieDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "Movie")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id")
    private int movieId;

    private String movienm;

    private int audiAcc;

    public static MovieEntity ToMovieEntity(MovieDTO movieDTO){
        return MovieEntity.builder()
                .id(movieDTO.getId())
                .movieId(movieDTO.getMovieId())
                .movienm(movieDTO.getMovienm())
                .audiAcc(movieDTO.getAudiAcc())
                .build();
    }
}
