package com.back.cinetalk.review.dto;

import lombok.*;

@Getter
public class ReviewRequestDTO {

    private Double star;

    private String content;

    private boolean spoiler;
}
