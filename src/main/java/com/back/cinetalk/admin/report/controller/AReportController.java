package com.back.cinetalk.admin.report.controller;

import com.back.cinetalk.admin.report.dto.AReportListDTO;
import com.back.cinetalk.admin.report.service.AReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/areports")
@RequiredArgsConstructor
public class AReportController {

    private final AReportService reportService;

    @GetMapping("/list")
    @Operation(summary = "관리자:신고 받은 댓글 리스트",description = "신고 받은 댓글 리스트 최신순 오름차순으로 출력")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공",content = @Content(schema = @Schema(implementation = AReportListDTO.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public ResponseEntity<?> aReportList() {

        return reportService.aReportList();
    }
}
