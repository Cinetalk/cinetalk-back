package com.back.cinetalk.review.controller;

import com.back.cinetalk.review.dto.ReviewRequestDTO;
import com.back.cinetalk.review.dto.ReviewResponseDTO;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "리뷰 등록", description = "리뷰 등록하는 프로세스")
    @ApiResponse(responseCode = "200", description = "등록완료",
            content = @Content(schema = @Schema(implementation = ReviewRequestDTO.class)))
    public ReviewResponseDTO reviewSave(HttpServletRequest request, @PathVariable Long movieId, @RequestBody ReviewRequestDTO reviewRequestDTO) {

        ReviewEntity reviewEntity = reviewService.reviewSave(request, movieId, reviewRequestDTO);
        return ReviewResponseDTO.toReviewResponseDTO(reviewEntity);
    }
}
