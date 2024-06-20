package com.back.cinetalk.user.controller;

import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.service.ReIssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.http.HttpResponse;

@RestController
@RequiredArgsConstructor
public class ReIssueController {

    private final ReIssueService reIssueService;

    @Operation(summary = "refresh 토큰 재발급",description = "access 토큰이 분실,만기시 재발급 받는 api")
    @ApiResponse(responseCode = "200",description = "재발급 설공")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        return reIssueService.reissueToken(request, response);
    }

    @GetMapping("/AuthBy")
    @Operation(summary = "로그인 성공시 토큰활용",description = "auth토큰을 활용한 유저정보 발급")
    @ApiResponse(responseCode = "200",description = "출력 성공",content = @Content(schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<?> AuthBy(@RequestParam(name = "authToken",required = false)String authToken, HttpServletResponse response){

        return reIssueService.AuthBy(response,authToken);
    }
}
