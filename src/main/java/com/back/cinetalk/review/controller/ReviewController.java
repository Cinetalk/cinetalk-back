package com.back.cinetalk.review.controller;

import com.back.cinetalk.review.dto.ReviewDTO;
import com.back.cinetalk.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/review")

@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/save")
    @Operation(summary = "리뷰 등록",description = "리뷰 등록하는 프로세스")
    @ApiResponse(responseCode = "200",description = "등록완료",
                content = @Content(schema = @Schema(implementation = ReviewDTO.class)))
    public ResponseEntity<?> ReviewSave(ReviewDTO reviewDTO){

        reviewService.reviewSave(reviewDTO);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
