package com.back.cinetalk.keyword.service;

import com.back.cinetalk.keyword.dto.KeywordRequestDTO;
import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public KeywordEntity create(KeywordRequestDTO keywordRequestDTO, String movieId) {
        // 로그인 검사 로직

        KeywordEntity keywordEntity = keywordRepository.findByKeywordAndMovieId(keywordRequestDTO.getKeyword(), movieId);

        System.out.println("keywordEntity = " + keywordEntity);

        if (keywordEntity != null) {
            keywordEntity.setCount(keywordEntity.getCount() + 1);
            return keywordEntity;
        } else {
            return keywordRepository.save(KeywordEntity.builder()
                    .movieId(movieId)
                    .keyword(keywordRequestDTO.getKeyword())
                    .count(1)
                    .build());
        }
    }

    @Transactional(readOnly = true)
    public List<KeywordEntity> getKeywordList(String movieId) {

        return keywordRepository.findAllByMovieIdOrderByCountDesc(movieId);
    }
}
