package com.back.cinetalk.report.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class KeywordReportRequestDTO {

    @NotNull
    private String content;
}
