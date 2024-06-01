package com.back.cinetalk.keyword.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class KeywordRequestDTO {

    @NotBlank
    private String keyword;
}
