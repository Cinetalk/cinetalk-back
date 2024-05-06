package com.back.cinetalk.movie.dto;

import com.back.cinetalk.movie.entity.MovieEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "영화 데이터 담기는 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private Long id;

    private String movie_id;

    private String movienm;

    private LocalDate regdate;

    public static MovieDTO ToMovieDTO(MovieEntity movieEntity){
        return MovieDTO.builder()
                .id(movieEntity.getId())
                .movie_id(movieEntity.getMovie_id())
                .movienm(movieEntity.getMovienm())
                .regdate(movieEntity.getRegdate())
                .build();
    }
}
