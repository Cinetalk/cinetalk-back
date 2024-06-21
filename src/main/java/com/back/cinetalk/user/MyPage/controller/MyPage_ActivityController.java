package com.back.cinetalk.user.MyPage.controller;

import com.back.cinetalk.user.MyPage.dto.activity.BadgeByUserResponseDTO;
import com.back.cinetalk.user.MyPage.dto.activity.CountSumByUserResponseDTO;
import com.back.cinetalk.user.MyPage.dto.activity.LogByUserResponseDTO;
import com.back.cinetalk.user.MyPage.dto.activity.ReviewByUserResponseDTO;
import com.back.cinetalk.user.MyPage.service.MyPage_ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyPage_ActivityController {

    private final MyPage_ActivityService myPage_activityService;

    @GetMapping("/BadgeByUser")
    @Operation(summary = "유저의 뱃지 목록",description = "토큰과 같이 요청시 유저의 뱃지 목록 반환")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공",content = @Content(schema = @Schema(implementation = BadgeByUserResponseDTO.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public ResponseEntity<?> BadgeByUser(HttpServletRequest request){return myPage_activityService.BadgeByUser(request);}

    @GetMapping("/CountSumByUser")
    @Operation(summary = "유저의 좋아요,댓글,찜 갯수",description = "토큰과 같이 요청시 유저의 좋아요,댓글,찜 갯수 반환")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공",content = @Content(schema = @Schema(implementation = CountSumByUserResponseDTO.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public ResponseEntity<?> CountSumByUser(HttpServletRequest request){return myPage_activityService.CountSumByUser(request);}

    @GetMapping("/ReviewByUser/{sort}")
    @Operation(summary = "유저의 최근댓글 조회",description = "토큰과 같이 요청시 유저의 최근댓글 조회")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공",content = @Content(schema = @Schema(implementation = ReviewByUserResponseDTO.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public ResponseEntity<?> ReviewByUser(@PathVariable(name = "sort",required = false) String sort, HttpServletRequest request) throws IOException {return myPage_activityService.ReviewByUser(sort,request);}

    @GetMapping("/LogByUser/{sort}")
    @Operation(summary = "유저의 평가 로그 조회",description = "토큰과 같이 요청시 유저의 평가 로그 날짜 정렬 해서 출력")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공",content = @Content(schema = @Schema(implementation = LogByUserResponseDTO.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public ResponseEntity<?> LogByUser(@PathVariable(name = "sort",required = false) String sort,HttpServletRequest request) throws IOException {return myPage_activityService.LogByUser(sort,request);}

}
