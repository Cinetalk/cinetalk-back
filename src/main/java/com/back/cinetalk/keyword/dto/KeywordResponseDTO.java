package com.back.cinetalk.keyword.dto;

import com.back.cinetalk.keyword.entity.KeywordEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordResponseDTO {

    private Long keywordId;

    private String keyword;

    private int count;

    public static KeywordResponseDTO toKeywordResponseDTO(KeywordEntity keywordEntity) {
        return KeywordResponseDTO.builder()
                .keywordId(keywordEntity.getId())
                .keyword(keywordEntity.getKeyword())
                .count(keywordEntity.getCount())
                .build();
    }
}
