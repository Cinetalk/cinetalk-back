package com.back.cinetalk.review.controller;

import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.review.dto.*;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.service.ReviewService;
import com.back.cinetalk.user.jwt.JwtValidation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{movieId}/save")
    @Operation(summary = "리뷰 등록 API", description = "리뷰를 등록하는 API 입니다.")
    public ReviewResponseDTO saveReview(@PathVariable(name = "movieId") Long movieId,
                                        @RequestBody @Valid ReviewRequestDTO reviewRequestDTO,
                                        @JwtValidation String email) {

        ReviewEntity reviewEntity = reviewService.saveReview(movieId, reviewRequestDTO, email);
        return ReviewResponseDTO.toReviewResponseDTO(reviewEntity);
    }

    @PostMapping("/{parentReviewId}/comments")
    @Operation(summary = "리뷰의 댓글 등록 API", description = "리뷰의 댓글을 등록하는 API 입니다.")
    public CommentResponseDTO saveComment(@PathVariable(name = "parentReviewId") Long parentReviewId,
                                          @RequestBody @Valid CommentRequestDTO commentRequestDTO,
                                          @JwtValidation String email) {

        ReviewEntity reReviewEntity = reviewService.saveComment(parentReviewId, commentRequestDTO, email);
        return CommentResponseDTO.toCommentResponseDTO(reReviewEntity);
    }

    @GetMapping("/{movieId}")
    @Operation(summary = "특정 영화의 리뷰 목록 조회 API", description = "특정 영화 전페 리뷰 목록을 조회하는 API 이며, 페이징을 포함합니다.")
    public ReviewPreViewListDTO getReviewListByMovie(@PathVariable(name = "movieId") Long movieId,
                                                     @RequestParam(name = "page") Integer page) {

        Page<ReviewPreViewDTO> reviewList = reviewService.getReviewList(movieId, page);
        return ReviewPreViewListDTO.toReviewPreViewListDTO(reviewList);
    }

    @GetMapping("/{movieId}/best")
    @Operation(summary = "특정 영화의 Best 리뷰 목록 조회 API", description = "특정 영화의 Best 리뷰 목록을 조회하는 API 입니다.")
    public List<ReviewPreViewDTO> getBestReviewsByMovie(@PathVariable(name = "movieId") Long movieId) {
        return reviewService.getBestReviews(movieId);
    }

    @GetMapping("/{movieId}/general")
    @Operation(summary = "특정 영화의 일반 리뷰 목록 조회 API", description = "특정 영화의 일반 리뷰 목록(Best 리뷰 제외)을 조회하는 API 이며, 페이징을 포함합니다.")
    public ReviewPreViewListDTO getGeneralReviewsByMovie(@PathVariable(name = "movieId") Long movieId,
                                                         @RequestParam(name = "page") Integer page) {

        Page<ReviewPreViewDTO> reviewListExcludingBest = reviewService.getGeneralReviewsExcludingBest(movieId, page);
        return ReviewPreViewListDTO.toReviewPreViewListDTO(reviewListExcludingBest);
    }

    @GetMapping("/{parentReviewId}/comments")
    @Operation(summary = "리뷰의 댓글 목록 조회 API", description = "특정 리뷰의 댓글 목록을 조회하는 API 이며, 페이징을 포함합니다.")
    public CommentPreViewListDTO getCommentListByParentReview(@PathVariable(name = "parentReviewId") Long parentReviewId,
                                                              @RequestParam(name = "page") Integer page) {

        Page<CommentPreViewDTO> reReviewList = reviewService.getCommentList(parentReviewId, page);
        return CommentPreViewListDTO.toReReviewPreViewListDTO(reReviewList);
    }

    @PatchMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정 API", description = "리뷰를 수정하는 API 입니다.")
    public ReviewResponseDTO updateReview(@PathVariable(name = "reviewId") Long reviewId,
                                          @RequestBody @Valid ReviewRequestDTO reviewRequestDTO,
                                          @JwtValidation String email) {

        ReviewEntity reviewEntity = reviewService.updateReview(reviewId, reviewRequestDTO, email);
        return ReviewResponseDTO.toReviewResponseDTO(reviewEntity);
    }

    @PatchMapping("/comments/{commentId}")
    @Operation(summary = "댓글 수정 API", description = "댓글을 수정하는 API 입니다.")
    public CommentResponseDTO updateComment(@PathVariable(name = "commentId") Long commentId,
                                            @RequestBody @Valid CommentRequestDTO commentRequestDTO,
                                            @JwtValidation String email) {

        ReviewEntity reviewEntity = reviewService.updateComment(commentId, commentRequestDTO, email);
        return CommentResponseDTO.toCommentResponseDTO(reviewEntity);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 or 댓글 삭제 API", description = "리뷰 혹은 댓글을 삭제하는 API 입니다.")
    public StateRes deleteReview(@PathVariable(name = "reviewId") Long reviewId,
                                 @JwtValidation String email) {

        return reviewService.deleteReview(reviewId, email);
    }

    @PostMapping("/{reviewId}/like")
    @Operation(summary = "리뷰 or 댓글 좋아요 API", description = "리뷰 혹은 댓글을 좋아요하는 API 입니다. 싫어요 API 요청 혹은 두번 API 요청시 좋아요가 취소됩니다.")
    public StateRes likeReview(@PathVariable Long reviewId, @JwtValidation String email) {

        return reviewService.likeReview(reviewId, email);
    }

    @PostMapping("/{reviewId}/dislike")
    @Operation(summary = "리뷰 or 댓글 싫어요 API", description = "리뷰 혹은 댓글을 싫어요하는 API 입니다. 좋아요 API 요청 홋은 두번 API 요청시 싫어요가 취소됩니다.")
    public StateRes dislikeReview(@PathVariable Long reviewId, @JwtValidation String email) {

        return reviewService.dislikeReview(reviewId, email);
    }

    @GetMapping("/my")
    @Operation(summary = "내가 작성한 리뷰 조회 API", description = "특정 영화의 내가 작성한 리뷰를 조회하는 API 입니다.")
    public MyReviewResponseDTO getMyReviewByMovie(@RequestParam Long movieId, @JwtValidation String email) {

        ReviewEntity myReviewByMovie = reviewService.getMyReviewByMovie(movieId, email);
        return MyReviewResponseDTO.toMyReviewResponseDTO(myReviewByMovie);
    }
}
