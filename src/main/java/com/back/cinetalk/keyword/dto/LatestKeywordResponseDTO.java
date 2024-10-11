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
    private Long keywordId;

    private String keyword;

    public static LatestKeywordResponseDTO toLatestKeywordResponseDTO(KeywordEntity keywordEntity) {
        return LatestKeywordResponseDTO.builder()
                .keywordId(keywordEntity.getId())
                .keyword(keywordEntity.getKeyword())
                .build();
    }

    public static List<LatestKeywordResponseDTO> fromEntityList(List<KeywordEntity> keywordEntityList) {
        return keywordEntityList.stream()
                .map(LatestKeywordResponseDTO::toLatestKeywordResponseDTO)
                .collect(Collectors.toList());
    }
}
