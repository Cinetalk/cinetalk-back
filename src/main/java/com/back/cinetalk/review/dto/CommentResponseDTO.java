package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {

    Long reviewId;

    LocalDateTime createdAt;

    public static CommentResponseDTO toCommentResponseDTO(ReviewEntity reviewEntity) {
        return CommentResponseDTO.builder()
                .reviewId(reviewEntity.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
