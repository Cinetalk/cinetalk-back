package com.back.cinetalk.review.service;

import com.back.cinetalk.badge.entity.BadgeEntity;
import com.back.cinetalk.badge.repository.BadgeRepository;
import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.genre.entity.GenreEntity;
import com.back.cinetalk.genre.repository.GenreRepository;
import com.back.cinetalk.rate.dislike.entity.ReviewDislikeEntity;
import com.back.cinetalk.rate.dislike.repository.ReviewDislikeRepository;
import com.back.cinetalk.rate.like.entity.ReviewLikeEntity;
import com.back.cinetalk.rate.like.repository.ReviewLikeRepository;
import com.back.cinetalk.review.dto.*;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.repository.ReviewRepository;
import com.back.cinetalk.reviewGenre.entity.ReviewGenreEntity;
import com.back.cinetalk.reviewGenre.repository.ReviewGenreRepository;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.repository.UserRepository;
import com.back.cinetalk.userBadge.entity.UserBadgeEntity;
import com.back.cinetalk.userBadge.repository.UserBadgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final ReviewGenreRepository reviewGenreRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final BadgeRepository badgeRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewDislikeRepository reviewDislikeRepository;

    @Transactional
    public ReviewEntity saveReview(Long movieId, ReviewRequestDTO reviewRequestDTO, String email) {
        UserEntity user = userRepository.findByEmail(email);

        if (reviewRepository.existsByUserIdAndMovieIdAndParentReviewIsNull(user.getId(), movieId)) {
            throw new RestApiException(CommonErrorCode.REVIEW_ALREADY_IN_WRITE);
        }

        List<Long> genreList = reviewRequestDTO.getGenreList();
        List<GenreEntity> genreEntities = genreRepository.findAllById(genreList);

        if (genreEntities.size() != genreList.size()) {
            throw new RestApiException(CommonErrorCode.GENRE_NOT_FOUND);
        }

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .movieId(movieId)
                .movienm(reviewRequestDTO.getMovieName())
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

        genreEntities.forEach(genre -> checkAndIssueGenreBadge(user, genre));

        return savedReview;
    }

    @Transactional
    public void checkAndIssueGenreBadge(UserEntity user, GenreEntity genre) {
        // 특정 장르에 대한 리뷰 수 계산
        long genreReviewCount = reviewRepository.countByUserAndGenre(user, genre);

        // 리뷰 수가 10개 이상인 경우 뱃지 발급
        if (genreReviewCount >= 10) {
            BadgeEntity badge = badgeRepository.findByGenre(genre)
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.BADGE_NOT_FOUND));

            if (badge != null) {
                // 이미 해당 장르 뱃지를 발급받았는지 확인
                Optional<UserBadgeEntity> existingBadge = userBadgeRepository.findByUserAndBadge(user, badge);

                if (existingBadge.isEmpty()) {
                    UserBadgeEntity userBadge = UserBadgeEntity.builder()
                            .user(user)
                            .badge(badge)
                            .isUse(false)
                            .build();
                    userBadgeRepository.save(userBadge);

                    log.info(genre.getName() + "장르의 뱃지가 발급되었습니다!");
                }
            }
        }
    }

    @Transactional
    public ReviewEntity saveComment(Long parentReviewId, CommentRequestDTO commentRequestDTO, String email) {
        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity parentReview = reviewRepository.findById(parentReviewId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.REVIEW_NOT_FOUND));

        ReviewEntity comment = ReviewEntity.builder()
                .user(user)
                .movieId(parentReview.getMovieId())
                .content(commentRequestDTO.getContent())
                .parentReview(parentReview)
                .build();

        return reviewRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Page<ReviewPreViewDTO> getReviewList(Long movieId, String email, Integer page, String sort) {
        Long userId = getUserIdFromEmail(email);
        return reviewRepository.findAllByMovieId(movieId, userId, PageRequest.of(page, 10), sort);
    }

    @Transactional(readOnly = true)
    public List<ReviewPreViewDTO> getBestReviews(Long movieId, String email) {
        Long userId = getUserIdFromEmail(email);
        int bestReviewLimit = 3;
        return reviewRepository.findBestReviews(movieId, userId, bestReviewLimit);
    }

    @Transactional(readOnly = true)
    public Page<ReviewPreViewDTO> getGeneralReviewsExcludingBest(Long movieId, String email, Integer page, String sort) {
        Long userId = getUserIdFromEmail(email);
        int bestReviewLimit = 3;

        // Best 리뷰 가져오기
        List<ReviewPreViewDTO> bestReviews = reviewRepository.findBestReviews(movieId, userId, bestReviewLimit);
        List<Long> bestReviewIds = bestReviews.stream()
                .map(ReviewPreViewDTO::getId)
                .toList();

        // 일반 리뷰 가져오기 (Best 리뷰 제외)
        return reviewRepository.findGeneralReviewsExcludingBest(movieId, userId, bestReviewIds, PageRequest.of(page, 10), sort);
    }

    @Transactional(readOnly = true)
    public Page<CommentPreViewDTO> getCommentList(Long parentReviewId, String email, Integer page) {
        Long userId = getUserIdFromEmail(email);
        return reviewRepository.findAllByParentReviewId(parentReviewId, userId, PageRequest.of(page, 10));
    }

    private Long getUserIdFromEmail(String email) {
        if (email == null) {
            return null;
        }
        UserEntity user = userRepository.findByEmail(email);
        return user != null ? user.getId() : null;
    }

    @Transactional
    public ReviewEntity updateReview(Long reviewId, ReviewRequestDTO reviewRequestDTO, String email) {
        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.REVIEW_NOT_FOUND));

        if (reviewEntity.getParentReview() != null) {
            throw new RestApiException(CommonErrorCode.REVIEW_NOT_ALLOWED);
        }

        verifyUserAuthorization(user, reviewEntity);

        reviewEntity.updateReview(reviewRequestDTO);
        return reviewEntity;
    }

    @Transactional
    public ReviewEntity updateComment(Long reviewId, CommentRequestDTO commentRequestDTO, String email) {
        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.COMMENT_NOT_FOUND));

        if (reviewEntity.getParentReview() == null) {
            throw new RestApiException(CommonErrorCode.REVIEW_NOT_ALLOWED);
        }

        verifyUserAuthorization(user, reviewEntity);

        reviewEntity.updateComment(commentRequestDTO);
        return reviewEntity;
    }

    @Transactional
    public StateRes deleteReview(Long reviewId, String email) {
        UserEntity user = userRepository.findByEmail(email);

        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.REVIEW_NOT_FOUND));

        verifyUserAuthorization(user, reviewEntity);

        // 리뷰 삭제
        reviewRepository.delete(reviewEntity);
        return new StateRes(true);
    }

    private void verifyUserAuthorization(UserEntity user, ReviewEntity reviewEntity) {
        if (!user.equals(reviewEntity.getUser())) {
            throw new RestApiException(CommonErrorCode.REVIEW_NOT_ALLOWED);
        }
    }

    @Transactional
    public StateRes likeReview(Long reviewId, String email) {
        UserEntity user = userRepository.findByEmail(email);

        // 리뷰에 대해 사용자가 이미 좋아요를 눌렀는지 확인
        if (reviewLikeRepository.existsByReviewIdAndUserId(reviewId, user.getId())) {
            // 이미 좋아요를 눌렀다면, 좋아요를 취소 (삭제)
            reviewLikeRepository.deleteByReviewIdAndUserId(reviewId, user.getId());
        } else {
            // 좋아요가 되어 있지 않다면, 싫어요를 눌렀는지 확인하고 삭제
            if (reviewDislikeRepository.existsByReviewIdAndUserId(reviewId, user.getId())) {
                reviewDislikeRepository.deleteByReviewIdAndUserId(reviewId, user.getId());
            }

            // 좋아요를 추가
            ReviewEntity review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.REVIEW_NOT_FOUND));
            ReviewLikeEntity like = ReviewLikeEntity.builder().review(review).user(user).build();
            reviewLikeRepository.save(like);
        }

        return new StateRes(true);
    }

    public StateRes checkLikeReview(Long reviewId, String email) {
        UserEntity user = userRepository.findByEmail(email);
        boolean checkLike = reviewLikeRepository.existsByReviewIdAndUserId(reviewId, user.getId());
        return new StateRes(checkLike);
    }

    @Transactional
    public StateRes dislikeReview(Long reviewId, String email) {
        UserEntity user = userRepository.findByEmail(email);

        // 리뷰에 대해 사용자가 이미 싫어요를 눌렀는지 확인
        if (reviewDislikeRepository.existsByReviewIdAndUserId(reviewId, user.getId())) {
            // 이미 싫어요를 눌렀다면, 싫어요를 취소 (삭제)
            reviewDislikeRepository.deleteByReviewIdAndUserId(reviewId, user.getId());
        } else {
            // 싫어요가 되어 있지 않다면, 좋아요를 눌렀는지 확인하고 삭제
            if (reviewLikeRepository.existsByReviewIdAndUserId(reviewId, user.getId())) {
                reviewLikeRepository.deleteByReviewIdAndUserId(reviewId, user.getId());
            }

            // 싫어요를 추가
            ReviewEntity review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new RestApiException(CommonErrorCode.REVIEW_NOT_FOUND));
            ReviewDislikeEntity dislike = ReviewDislikeEntity.builder().review(review).user(user).build();
            reviewDislikeRepository.save(dislike);
        }

        return new StateRes(true);
    }

    public StateRes checkDislikeReview(Long reviewId, String email) {
        UserEntity user = userRepository.findByEmail(email);
        boolean checkDislike = reviewDislikeRepository.existsByReviewIdAndUserId(reviewId, user.getId());
        return new StateRes(checkDislike);
    }

    @Transactional(readOnly = true)
    public ReviewEntity getMyReviewByMovieId(Long movieId, String email) {
        UserEntity user = userRepository.findByEmail(email);
        return reviewRepository.findByUserIdAndMovieIdAndParentReviewIsNull(user.getId(), movieId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.REVIEW_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ReviewEntity getMyStarByMovieId(Long movieId, String email) {
        UserEntity user = userRepository.findByEmail(email);
        return reviewRepository.findByUserIdAndMovieIdAndParentReviewIsNull(user.getId(), movieId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.REVIEW_NOT_FOUND));
    }
}
