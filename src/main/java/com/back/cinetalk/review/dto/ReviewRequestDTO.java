package com.back.cinetalk.review.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
public class ReviewRequestDTO {
    @NotNull
    private String movieName;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "5.0", inclusive = true)
    private Double star;

    @NotBlank
    private String content;

    @NotNull
    private boolean spoiler;

    private List<Long> genreList;
}
