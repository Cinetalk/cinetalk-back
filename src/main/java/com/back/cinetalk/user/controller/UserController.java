package com.back.cinetalk.user.controller;

import com.back.cinetalk.user.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    public final JWTUtil jwtUtil;

    @GetMapping("/user")
    public String UserP(){

        return "user인증됨";
    }

    @PostMapping("/userInfo")
    public String UserInfo(HttpServletRequest request){

        String accessToken= request.getHeader("access");

        String email = jwtUtil.getEmail(accessToken);


        return email;
    }
}
