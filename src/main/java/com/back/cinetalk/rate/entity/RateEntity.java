package com.back.cinetalk.rate.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.rate.dto.RateDTO;
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

    private Long reviewId;

    @ColumnDefault("0")
    private Long rereviewId;

    private Long userId;

    private int rate;

    public static RateEntity ToRateEntity(RateDTO rateDTO){
        return RateEntity.builder()
                .id(rateDTO.getId())
                .reviewId(rateDTO.getReviewId())
                .rereviewId(rateDTO.getRereviewId())
                .userId(rateDTO.getUserId())
                .rate(rateDTO.getRate())
                .build();
    }
}
