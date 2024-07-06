package com.back.cinetalk.report.controller;

import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.report.dto.ReportRequestDTO;
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

    @PostMapping("/{reviewId}")
    @Operation(summary = "리뷰 신고 API", description = "리뷰를 신고하는 API 입니다.")
    public StateRes saveReport(@PathVariable(name = "reviewId") Long reviewId,
                               @RequestBody @Valid ReportRequestDTO reportRequestDTO,
                               @JwtValidation String email) {

        return reportService.saveReport(reviewId, reportRequestDTO, email);
    }
}
