package com.back.cinetalk.keyword.controller;

import com.back.cinetalk.keyword.dto.KeywordRequestDTO;
import com.back.cinetalk.keyword.dto.KeywordResponseDTO;
import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.keyword.service.KeywordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("keywords")
public class KeywordController {

    private final KeywordService keywordService;

    @PostMapping("/{movieId}")
    public KeywordResponseDTO createKeyword(@PathVariable String movieId,
                                            @RequestBody @Valid KeywordRequestDTO keywordRequestDTO) {

        KeywordEntity keywordEntity = keywordService.create(keywordRequestDTO, movieId);
        return KeywordResponseDTO.toKeywordResponseDTO(keywordEntity);
    }

    @GetMapping("/{movieId}")
    public List<KeywordResponseDTO> getKeywordList(@PathVariable String movieId) {

        List<KeywordEntity> keywordEntityList = keywordService.getKeywordList(movieId);

        return keywordEntityList.stream()
                .map(keyword ->
                        KeywordResponseDTO.builder()
                                .keyword(keyword.getKeyword())
                                .count(keyword.getCount())
                                .build()
                ).collect(Collectors.toList());
    }
}
