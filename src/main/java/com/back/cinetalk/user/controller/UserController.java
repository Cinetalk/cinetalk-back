package com.back.cinetalk.user.controller;

import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public String UserP(){

        return "user인증됨";
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
