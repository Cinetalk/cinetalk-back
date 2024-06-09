package com.back.cinetalk.review.controller;

import com.back.cinetalk.review.dto.ReviewRequestDTO;
import com.back.cinetalk.review.dto.ReviewResponseDTO;
import com.back.cinetalk.review.dto.StateRes;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")

@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{movieId}/save")
    @Operation(summary = "리뷰 등록 API", description = "리뷰를 등록하는 프로세스")
    @ApiResponse(responseCode = "200", description = "등록완료",
            content = @Content(schema = @Schema(implementation = ReviewRequestDTO.class)))
    public ReviewResponseDTO saveReview(HttpServletRequest request, @PathVariable Long movieId, @RequestBody ReviewRequestDTO reviewRequestDTO) {

        ReviewEntity reviewEntity = reviewService.saveReview(request, movieId, reviewRequestDTO);
        return ReviewResponseDTO.toReviewResponseDTO(reviewEntity);
    }

    @PatchMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정 API", description = "리뷰를 수정하는 프로세스")
    public ReviewResponseDTO updateReview(HttpServletRequest request, @PathVariable Long reviewId, @RequestBody ReviewRequestDTO reviewRequestDTO) {

        ReviewEntity reviewEntity = reviewService.updateReview(request, reviewId, reviewRequestDTO);
        return ReviewResponseDTO.toReviewResponseDTO(reviewEntity);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제 API", description = "리뷰를 삭제하는 프로세스")
    public StateRes deleteReview(HttpServletRequest request, @PathVariable Long reviewId) {
        return reviewService.deleteReview(request, reviewId);
    }

}
