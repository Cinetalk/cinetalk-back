package com.back.cinetalk.keyword.dto;

import com.back.cinetalk.keyword.entity.KeywordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<LatestKeywordResponseDTO> fromEntityList(List<String> keywordList) {
        return keywordList.stream()
                .map(LatestKeywordResponseDTO::toLatestKeywordResponseDTO)
                .collect(Collectors.toList());
    }
}
