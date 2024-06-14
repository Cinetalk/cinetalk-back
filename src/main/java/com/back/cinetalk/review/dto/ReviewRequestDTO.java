package com.back.cinetalk.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
public class ReviewRequestDTO {

    @NotNull
    private Double star;

    @NotBlank
    private String content;

    @NotNull
    private boolean spoiler;

    private List<Long> genreList;
}
