package com.back.cinetalk.keyword.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class KeywordRequestDTO {


    @Size(min = 1, max = 5)
    @NotBlank(message = "빈칸은 앙대")
    private String keyword;
}
