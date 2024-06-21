package com.back.cinetalk.user.MyPage.component;

import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserByAccess {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public UserEntity getUserEntity(HttpServletRequest request){
        String access = request.getHeader("access");

        String email = jwtUtil.getEmail(access);

        return userRepository.findByEmail(email);
    }
}
