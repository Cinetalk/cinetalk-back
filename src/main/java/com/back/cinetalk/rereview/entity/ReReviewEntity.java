package com.back.cinetalk.rereview.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.rereview.dto.ReReviewDTO;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.entity.UserEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ReviewEntity review;

    private String content;

//    public static ReReviewEntity ToReReviewEntity(ReReviewDTO rereviewDTO){
//        return ReReviewEntity.builder()
//                .id(rereviewDTO.getId())
//                .reviewId(rereviewDTO.getReviewId())
//                .userId(rereviewDTO.getUserId())
//                .content(rereviewDTO.getContent())
//                .build();
//    }
}
