package com.back.cinetalk.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
public class CommentRequestDTO {

    @NotBlank
    private String content;
}
