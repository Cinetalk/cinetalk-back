package com.back.cinetalk.user.controller;

import com.back.cinetalk.user.dto.BadgeByUserResponseDTO;
import com.back.cinetalk.user.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/BadgeByUser")
    @Operation(summary = "유저의 뱃지 목록",description = "토큰과 같이 요청시 유저의 뱃지 목록 반환")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공",content = @Content(schema = @Schema(implementation = BadgeByUserResponseDTO.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public ResponseEntity<?> BadgeByUser(HttpServletRequest request){return myPageService.BadgeByUser(request);}

    @GetMapping("/CountSumByUser")
    @Operation(summary = "유저의 좋아요,댓글,찜 갯수",description = "토큰과 같이 요청시 유저의 좋아요,댓글,찜 갯수 반환")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공")
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public ResponseEntity<?> CountSumByUser(HttpServletRequest request){return myPageService.CountSumByUser(request);}
}
