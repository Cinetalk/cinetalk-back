package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Schema(description = "리뷰 데이터 담기는 DTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;

    private int movie_id;

    private int member_id;

    private int star;

    private int content;

    private LocalDate regdate;

    public static ReviewDTO ToReviewDTO(ReviewEntity reviewEntity){
        return ReviewDTO.builder()
                .id(reviewEntity.getId())
                .movie_id(reviewEntity.getMovie_id())
                .member_id(reviewEntity.getMember_id())
                .star(reviewEntity.getStar())
                .content(reviewEntity.getContent())
                .regdate(reviewEntity.getRegdate())
                .build();
    }
}
