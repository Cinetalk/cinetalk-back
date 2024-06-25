package com.back.cinetalk.keyword.service;

import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.keyword.dto.KeywordRequestDTO;
import com.back.cinetalk.keyword.dto.KeywordResponseDTO;
import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.keyword.repository.KeywordRepository;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public KeywordEntity createKeyword(Long movieId, KeywordRequestDTO keywordRequestDTO, String email) {
        UserEntity user = userRepository.findByEmail(email);

        if (keywordRepository.existsByUserIdAndMovieId(user.getId(), movieId)) {
            throw new RestApiException(CommonErrorCode.KEYWORD_ALREADY_IN_WRITE);
        }

        return keywordRepository.save(KeywordEntity.builder()
                .movieId(movieId)
                .keyword(keywordRequestDTO.getKeyword())
                .user(user)
                .count(1)
                .build());
    }

    @Transactional(readOnly = true)
    public List<KeywordResponseDTO> getTopKeywordListByMovie(Long movieId) {
        return keywordRepository.findKeywordsOrderByCountDesc(movieId);
    }

    @Transactional(readOnly = true)
    public List<String> getLatestMentionedKeywordListByMovie(Long movieId) {
        return keywordRepository.findDistinctKeywordsByMovieIdOrderByCreatedAtDesc(movieId);
    }

    @Transactional
    public KeywordEntity updateKeyword(Long keywordId, KeywordRequestDTO keywordRequestDTO, String email) {
        UserEntity user = userRepository.findByEmail(email);

        KeywordEntity keywordEntity = keywordRepository.findById(keywordId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.KEYWORD_NOT_FOUND));

        if (!user.equals(keywordEntity.getUser())) {
            throw new RestApiException(CommonErrorCode.KEYWORD_NOT_ALLOWED);
        }

        keywordEntity.update(keywordRequestDTO);
        return keywordEntity;
    }
}
