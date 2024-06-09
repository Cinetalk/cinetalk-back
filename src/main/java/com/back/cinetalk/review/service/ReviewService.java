package com.back.cinetalk.review.service;

import com.back.cinetalk.review.dto.ReviewRequestDTO;
import com.back.cinetalk.review.dto.StateRes;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public ReviewEntity saveReview(HttpServletRequest request, Long movieId, ReviewRequestDTO reviewRequestDTO) {
        String email = jwtUtil.getEmail(request.getHeader("access"));
        UserEntity user = userRepository.findByEmail(email);

        if (reviewRepository.existsByUserIdAndMovieId(user.getId(), movieId)) {
            throw new RuntimeException("이미 작성한 리뷰입니다.");
        }

        ReviewEntity review = ReviewEntity.builder()
                .movieId(movieId)
//                .movienm()
                .userId(user.getId())
                .star(reviewRequestDTO.getStar())
                .content(reviewRequestDTO.getContent())
                .spoiler(reviewRequestDTO.isSpoiler())
                .build();

        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewEntity> getReviewList(Long movieId, Integer page) {
        return reviewRepository.findAllByMovieId(movieId, PageRequest.of(page, 10));
    }

    @Transactional
    public ReviewEntity updateReview(HttpServletRequest request, Long reviewId, ReviewRequestDTO reviewRequestDTO) {
        String email = jwtUtil.getEmail(request.getHeader("access"));
        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        if (!user.getId().equals(reviewEntity.getUserId())) {
            throw new SecurityException("리뷰를 수정할 권한이 없습니다.");
        }

        reviewEntity.update(reviewRequestDTO);
        return reviewEntity;
    }

    @Transactional
    public StateRes deleteReview(HttpServletRequest request, Long reviewId) {
        String email = jwtUtil.getEmail(request.getHeader("access"));
        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        if (!user.getId().equals(reviewEntity.getUserId())) {
            throw new SecurityException("리뷰를 수정할 권한이 없습니다.");
        }

        reviewRepository.delete(reviewEntity);
        return new StateRes(true);
    }

}
