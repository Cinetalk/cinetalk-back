package com.back.cinetalk.user.MyPage.controller;

import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.user.MyPage.service.MyPage_InfoService;
import com.back.cinetalk.user.dto.NickNameMergeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyPage_InfoController {

    private final MyPage_InfoService myPageInfoService;
    @GetMapping("/userInfo")
    @Operation(summary = "마이페이지 상단 유저정보",description = "토큰과 같이 요청시 유저 정보 반환")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공")
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public ResponseEntity<?> UserInfo(HttpServletRequest request){

        return myPageInfoService.UserInfo(request);
    }

    @GetMapping("/NicknameCheck")
    @Operation(summary = "마이페이지 닉네임 변경 체크",description = "닉네임 변경 시 유효한 닉네임인지 체크해서 반환하는 로직")
    @ApiResponse(responseCode = "200",description = "체크 성공",content = @Content(schema = @Schema(implementation = Boolean.class)))
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public Boolean NicknameCheck(@RequestParam(name = "nickname") String nickname){
        return myPageInfoService.NicknameCheck(nickname);
    }
    @PatchMapping("/nickNameMerge")
    @Operation(summary = "회원가입 닉네임 수정",description = "회원가입 시 닉네임,생년월일,성별을 수정하는 로직")
    @ApiResponse(responseCode = "200",description = "success",content = @Content(schema = @Schema(implementation = StateRes.class)))
    @ApiResponse(responseCode = "404",description = "닉네임이 옳바르지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public StateRes nickNameMerge(@Valid @RequestBody NickNameMergeDTO nameMergeDTO, HttpServletRequest request){

        return myPageInfoService.nickNameMerge(request,nameMergeDTO);
    }

    @PatchMapping("/userInfoMerge/{category}")
    @Operation(summary = "개인정보 수정",description = "마이페이지 닉네임,생년월일,성별을 수정하는 로직")
    @ApiResponse(responseCode = "200",description = "success",content = @Content(schema = @Schema(implementation = StateRes.class)))
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public StateRes userInfoMerge(@RequestParam(name = "value")String value,
                                            @PathVariable(name = "category",required = false) String category,HttpServletRequest request){

        return myPageInfoService.userInfoMerge(request,category,value);
    }

    @PatchMapping("/UserProfileChange")
    @Operation(summary = "사용자 프로필 수정",description = "사용자 프로필 사진 수정 처리")
    @ApiResponse(responseCode = "200",description = "success",content = @Content(schema = @Schema(implementation = StateRes.class)))
    @ApiResponse(responseCode = "404",description = "닉네임이 옳바르지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public StateRes UserProfileChange(@RequestParam(value = "profile",required = false) MultipartFile file,HttpServletRequest request){
        return myPageInfoService.UserProfileChange(request, file);
    }

    @DeleteMapping("/UserDelete")
    @Operation(summary = "회원 탈퇴",description = "회원 탈퇴 및 쿠키 삭제,회원이 등록한 데이터 삭제")
    @ApiResponse(responseCode = "200",description = "success",content = @Content(schema = @Schema(implementation = StateRes.class)))
    @ApiResponse(responseCode = "404",description = "닉네임이 옳바르지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    public StateRes UserDelete(HttpServletRequest request, HttpServletResponse response){
        return myPageInfoService.UserDelete(request, response);
    }
}
