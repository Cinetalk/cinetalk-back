package com.back.cinetalk.keyword.repository;

import com.back.cinetalk.keyword.dto.KeywordResponseDTO;

import java.util.List;

public interface KeywordRepositoryCustom {

    List<KeywordResponseDTO> findKeywordsOrderByCountDesc(Long movieId);
}
