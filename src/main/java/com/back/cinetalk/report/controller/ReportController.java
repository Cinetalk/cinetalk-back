package com.back.cinetalk.report.controller;

import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.report.dto.KeywordReportRequestDTO;
import com.back.cinetalk.report.dto.ReviewReportRequestDTO;
import com.back.cinetalk.report.service.ReportService;
import com.back.cinetalk.user.jwt.JwtValidation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/reviews/{reviewId}")
    @Operation(summary = "리뷰 신고 API", description = "리뷰를 신고하는 API 입니다.")
    public StateRes saveReviewReport(@PathVariable(name = "reviewId") Long reviewId,
                                     @RequestBody @Valid ReviewReportRequestDTO reviewReportRequestDTO,
                                     @JwtValidation String email) {

        return reportService.saveReviewReport(reviewId, reviewReportRequestDTO, email);
    }

    @PostMapping("/keywords/{keywordId}")
    @Operation(summary = "키워드 신고 API", description = "리뷰를 신고하는 API 입니다.")
    public StateRes saveKeywordReport(@PathVariable Long keywordId,
                                      @RequestBody @Valid KeywordReportRequestDTO keywordReportRequestDTO,
                                      @JwtValidation String email) {

        return reportService.saveKeywordReport(keywordId, keywordReportRequestDTO, email);
    }
}
