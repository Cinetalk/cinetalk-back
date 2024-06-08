package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
public class ReviewRequestDTO {

    private Double star;

    private String content;

}
