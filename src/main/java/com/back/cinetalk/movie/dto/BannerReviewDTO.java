package com.back.cinetalk.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BannerReviewDTO {

    private Double star;

    private String content;

    private LocalDateTime createdAt;
}
