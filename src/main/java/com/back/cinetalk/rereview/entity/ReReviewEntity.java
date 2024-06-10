package com.back.cinetalk.rereview.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.rereview.dto.ReReviewDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ReReview")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reviewId;

    private Long userId;

    private String content;

    public static ReReviewEntity ToReReviewEntity(ReReviewDTO rereviewDTO){
        return ReReviewEntity.builder()
                .id(rereviewDTO.getId())
                .reviewId(rereviewDTO.getReviewId())
                .userId(rereviewDTO.getUserId())
                .content(rereviewDTO.getContent())
                .build();
    }
}
