package com.back.cinetalk.keyword.controller;

import com.back.cinetalk.keyword.dto.KeywordRequestDTO;
import com.back.cinetalk.keyword.dto.KeywordResponseDTO;
import com.back.cinetalk.keyword.dto.LatestKeywordResponseDTO;
import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.keyword.service.KeywordService;
import com.back.cinetalk.user.jwt.JwtValidation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("keywords")
public class KeywordController {

    private final KeywordService keywordService;

    @PostMapping("/{movieId}/save")
    @Operation(summary = "특정 영화의 키워드 등록 API", description = "특정 영화의 키워드를 등록하는 API 입니다.")
    public KeywordResponseDTO createKeyword(@PathVariable(name = "movieId") Long movieId,
                                            @RequestBody @Valid KeywordRequestDTO keywordRequestDTO,
                                            @JwtValidation String email) {

        KeywordEntity keywordEntity = keywordService.createKeyword(movieId, keywordRequestDTO, email);
        return KeywordResponseDTO.toKeywordResponseDTO(keywordEntity);
    }

    @GetMapping("/{movieId}/myKeyword")
    @Operation(summary = "특정 영화에 내가 작성한 키워드 조회 API", description = "특정 영화의 내가 작성한 키워드를 조회하는 API 입니다.")
    public KeywordResponseDTO getMyKeywordByMovie(@PathVariable(name = "movieId") Long movieId,
                                            @JwtValidation String email) {

        KeywordEntity keywordEntity = keywordService.getMyKeywordByMovie(movieId, email);
        return KeywordResponseDTO.toKeywordResponseDTO(keywordEntity);
    }

    @GetMapping("/{movieId}")
    @Operation(summary = "특정 영화의 키워드 조회 API", description = "특정 영화의 많이 언급된 상위 26개의 키워드를 조회 하는 API 입니다.")
    public List<KeywordResponseDTO> getTopKeywordListByMovie(@PathVariable(name = "movieId") Long movieId) {

        return keywordService.getTopKeywordListByMovie(movieId);
    }

    @GetMapping("/latest/{movieId}")
    @Operation(summary = "특정 영화의 최신 언급된 키워드 조회 API", description = "가장 최신에 언급된 키워드 4개를 조회하는 API 입니다.")
    public List<LatestKeywordResponseDTO> getLatestMentionedKeywordListByMovie(@PathVariable(name = "movieId") Long movieId) {

        List<String> keywordEntityList = keywordService.getLatestMentionedKeywordListByMovie(movieId);
        return LatestKeywordResponseDTO.fromEntityList(keywordEntityList);
    }

    @PatchMapping("/{keywordId}")
    @Operation(summary = "특정 영화의 키워드 수정 API", description = "특정 영화의 키워드를 수정하는 API 입니다.")
    public KeywordResponseDTO updateKeyword(@PathVariable(name = "keywordId") Long keywordId,
                                            @RequestBody @Valid KeywordRequestDTO keywordRequestDTO,
                                            @JwtValidation String emai) {

        KeywordEntity keywordEntity = keywordService.updateKeyword(keywordId, keywordRequestDTO, emai);
        return KeywordResponseDTO.toKeywordResponseDTO(keywordEntity);
    }
}
