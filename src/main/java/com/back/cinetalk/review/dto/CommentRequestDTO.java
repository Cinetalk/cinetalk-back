package com.back.cinetalk.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
public class CommentRequestDTO {

    @NotBlank
    @Size(max = 2000)
    private String content;
}
