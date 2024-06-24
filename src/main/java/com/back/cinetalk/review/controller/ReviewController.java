package com.back.cinetalk.review.controller;

import com.back.cinetalk.review.dto.*;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.service.ReviewService;
import com.back.cinetalk.user.jwt.JwtValidation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "특정 영화의 리뷰 목록 조회 API", description = "특정 영화 리뷰들의 목록을 조회하는 API 이며, 페이징을 포함합니다.")
    public ReviewPreViewListDTO getReviewListByMovie(@PathVariable(name = "movieId") Long movieId,
                                                     @RequestParam(name = "page") Integer page) {

        Page<ReviewPreViewDTO> reviewList = reviewService.getReviewList(movieId, page);
        return ReviewPreViewListDTO.toReviewPreViewListDTO(reviewList);
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

}
