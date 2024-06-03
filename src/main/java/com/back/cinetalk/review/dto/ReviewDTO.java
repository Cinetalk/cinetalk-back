package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "리뷰 데이터 담기는 DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;

    private int movie_id;

    private String movie_name;

    private int user_id;

    private Double star;

    private String content;

    public static ReviewDTO ToReviewDTO(ReviewEntity reviewEntity){
        return ReviewDTO.builder()
                .id(reviewEntity.getId())
                .movie_id(reviewEntity.getMovie_id())
                .movie_name(reviewEntity.getMovie_name())
                .user_id(reviewEntity.getUser_id())
                .star(reviewEntity.getStar())
                .content(reviewEntity.getContent())
                .build();
    }
}
