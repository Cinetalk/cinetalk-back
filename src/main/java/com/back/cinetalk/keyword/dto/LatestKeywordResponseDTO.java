package com.back.cinetalk.keyword.dto;

import com.back.cinetalk.keyword.entity.KeywordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LatestKeywordResponseDTO {

    private String keyword;

    public static LatestKeywordResponseDTO toLatestKeywordResponseDTO(String keyword) {
        return LatestKeywordResponseDTO.builder()
                .keyword(keyword)
                .build();
    }
}
