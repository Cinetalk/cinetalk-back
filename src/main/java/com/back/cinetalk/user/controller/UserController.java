package com.back.cinetalk.user.controller;

import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "닉네임 설정",description = "회원가입 시 닉네임 설정")
    @ApiResponse(responseCode = "200",description = "success")
    @ApiResponse(responseCode = "404",description = "닉네임이 옳바르지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    @Parameter(name = "nickname",description = "닉네임",required = false)
    @PostMapping("/nickNameMerge")
    public ResponseEntity<?> nickNameMerge(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam(name = "nickname",required = false)String nickname){

        if(nickname == null){

            return new ResponseEntity<>("닉네임이 옳바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        return userService.nickNameMerge(request,nickname);
    }



    @Operation(summary = "User정보 발급",description = "토큰과 같이 요청시 유저 정보 반환")
    @ApiResponse(responseCode = "200",description = "정보 발급 성공")
    @ApiResponse(responseCode = "404",description = "토큰이 존재하지 않음")
    @ApiResponse(responseCode = "401",description = "토큰이 유효하지 않음")
    @PostMapping("/userInfo")
    public ResponseEntity<?> UserInfo(HttpServletRequest request, HttpServletResponse response){

        return userService.UserInfo(request,response);
    }
}
