package com.back.cinetalk.review.service;

import com.back.cinetalk.review.dto.ReviewRequestDTO;
import com.back.cinetalk.review.dto.ReviewResponseDTO;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public ReviewEntity reviewSave(HttpServletRequest request, Long movieId, ReviewRequestDTO reviewRequestDTO) {

//        String email = jwtUtil.getEmail(request.getHeader("access"));
//        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity review = ReviewEntity.builder()
                .movieId(movieId)
//                .userId(user.getId())
                .userId(1L)
                .star(reviewRequestDTO.getStar())
                .content(reviewRequestDTO.getContent())
                .build();

        return reviewRepository.save(review);
    }
}
