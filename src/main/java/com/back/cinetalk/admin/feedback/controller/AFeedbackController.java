package com.back.cinetalk.admin.feedback.controller;

import com.back.cinetalk.admin.feedback.dto.FeedBackListDTO;
import com.back.cinetalk.admin.feedback.service.AFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/afeedback")
@RequiredArgsConstructor
public class AFeedbackController {

    private final AFeedbackService feedbackService;

    @GetMapping("/feedBackList")
    @Operation(summary = "관리자: 6개월간 가입한 회원 수 ",description = "이번달을 포함한 지난 6달 간의 가입한 회원 수")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공",content = @Content(schema = @Schema(implementation = FeedBackListDTO.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public ResponseEntity<?> feedBackList(){

        return feedbackService.feedBackList();
    }
}
