package com.back.cinetalk.rate.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.rate.dto.RateDTO;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Rate")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private int rate;

    public static RateEntity ToRateEntity(RateDTO rateDTO){
        return RateEntity.builder()
                .id(rateDTO.getId())
                .review(rateDTO.getReview())
                .user(rateDTO.getUser())
                .rate(rateDTO.getRate())
                .build();
    }
}
