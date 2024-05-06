package com.back.cinetalk.movie.entity;

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
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String movie_id;

    private String movienm;

    private LocalDate regdate;

    @PrePersist
    public void prePersist() {
        this.regdate = LocalDate.now(); // 현재 날짜를 설정
    }

    public static MovieEntity ToMovieEntity(MovieDTO movieDTO){
        return MovieEntity.builder()
                .id(movieDTO.getId())
                .movie_id(movieDTO.getMovie_id())
                .movienm(movieDTO.getMovienm())
                .regdate(movieDTO.getRegdate())
                .build();
    }
}
