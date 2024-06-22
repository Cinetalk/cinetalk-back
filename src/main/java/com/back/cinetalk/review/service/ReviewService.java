package com.back.cinetalk.review.service;

import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.exception.handler.ReviewHandler;
import com.back.cinetalk.genre.entity.GenreEntity;
import com.back.cinetalk.genre.repository.GenreRepository;
import com.back.cinetalk.review.dto.*;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.reviewGenre.entity.ReviewGenreEntity;
import com.back.cinetalk.reviewGenre.repository.ReviewGenreRepository;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final ReviewGenreRepository reviewGenreRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public ReviewEntity saveReview(HttpServletRequest request, Long movieId, ReviewRequestDTO reviewRequestDTO) {
        String email = jwtUtil.getEmail(request.getHeader("access"));
        UserEntity user = userRepository.findByEmail(email);

        if (reviewRepository.existsByUserIdAndMovieId(user.getId(), movieId)) {
            throw new RestApiException(CommonErrorCode.REVIEW_ALREADY_IN_WRITE);
        }

        List<Long> genreList = reviewRequestDTO.getGenreList();
        List<GenreEntity> genreEntities = genreRepository.findAllById(genreList);

        if (genreEntities.size() != genreList.size()) {
            throw new RestApiException(CommonErrorCode.GENRE_NOT_FOUND);
        }

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .movieId(movieId)
                .user(user)
                .star(reviewRequestDTO.getStar())
                .content(reviewRequestDTO.getContent())
                .spoiler(reviewRequestDTO.isSpoiler())
                .parentReview(null)
                .build();

        ReviewEntity savedReview = reviewRepository.save(reviewEntity);

        genreEntities.stream()
                .map(genreEntity -> ReviewGenreEntity.builder()
                        .review(savedReview)
                        .genre(genreEntity)
                        .build())
                .forEach(reviewGenreRepository::save);

        return savedReview;

    }

    @Transactional
    public ReviewEntity saveComment(HttpServletRequest request, Long parentReviewId, CommentRequestDTO commentRequestDTO) {
        String email = jwtUtil.getEmail(request.getHeader("access"));
        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity parentReview = reviewRepository.findById(parentReviewId)
                .orElseThrow(() -> new ReviewHandler(CommonErrorCode.REVIEW_NOT_FOUND));

        ReviewEntity comment = ReviewEntity.builder()
                .user(user)
                .movieId(parentReview.getMovieId())
                .content(commentRequestDTO.getContent())
                .parentReview(parentReview)
                .build();

        return reviewRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Page<ReviewPreViewDTO> getReviewList(Long movieId, Integer page) {
        return reviewRepository.findAllByMovieId(movieId, PageRequest.of(page, 10));
    }

    @Transactional(readOnly = true)
    public Page<CommentPreViewDTO> getCommentList(Long parentReviewId, Integer page) {
        return reviewRepository.findAllByParentReviewId(parentReviewId, PageRequest.of(page, 10));
    }

    @Transactional
    public ReviewEntity updateReview(HttpServletRequest request, Long reviewId, ReviewRequestDTO reviewRequestDTO) {
        String email = jwtUtil.getEmail(request.getHeader("access"));
        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewHandler(CommonErrorCode.REVIEW_NOT_FOUND));

        if (reviewEntity.getParentReview() != null) {
            throw new RestApiException(CommonErrorCode.REVIEW_NOT_ALLOWED);
        }

        verifyUserAuthorization(user, reviewEntity);

        reviewEntity.updateReview(reviewRequestDTO);
        return reviewEntity;
    }

    @Transactional
    public ReviewEntity updateComment(HttpServletRequest request, Long reviewId, CommentRequestDTO commentRequestDTO) {
        String email = jwtUtil.getEmail(request.getHeader("access"));
        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewHandler(CommonErrorCode.COMMENT_NOT_FOUND));

        if (reviewEntity.getParentReview() == null) {
            throw new RestApiException(CommonErrorCode.REVIEW_NOT_ALLOWED);
        }

        verifyUserAuthorization(user, reviewEntity);

        reviewEntity.updateComment(commentRequestDTO);
        return reviewEntity;
    }

    @Transactional
    public StateRes deleteReview(HttpServletRequest request, Long reviewId) {
        String email = jwtUtil.getEmail(request.getHeader("access"));
        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewHandler(CommonErrorCode.REVIEW_NOT_FOUND));

        verifyUserAuthorization(user, reviewEntity);

        reviewRepository.delete(reviewEntity);
        return new StateRes(true);
    }

    private void verifyUserAuthorization(UserEntity user, ReviewEntity reviewEntity) {
        if (!user.equals(reviewEntity.getUser())) {
            throw new RestApiException(CommonErrorCode.REVIEW_NOT_ALLOWED);
        }
    }

}
