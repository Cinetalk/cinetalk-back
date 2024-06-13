package com.back.cinetalk.keyword.service;

import com.back.cinetalk.keyword.dto.KeywordRequestDTO;
import com.back.cinetalk.keyword.dto.KeywordResponseDTO;
import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.keyword.repository.KeywordRepository;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    public KeywordEntity create(HttpServletRequest request, Long movieId, KeywordRequestDTO keywordRequestDTO) {
        // 로그인 검사 로직
//        String email = jwtUtil.getEmail(request.getHeader("access"));
//        UserEntity user = userRepository.findByEmail(email);

        return keywordRepository.save(KeywordEntity.builder()
                .movieId(movieId)
                .keyword(keywordRequestDTO.getKeyword())
//                .user(user)
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
}
