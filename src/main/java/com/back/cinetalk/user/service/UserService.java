package com.back.cinetalk.user.service;

import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.RefreshRepository;
import com.back.cinetalk.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    public final JWTUtil jwtUtil;

    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> UserInfo(HttpServletRequest request, HttpServletResponse response){

        log.info("User정보 로직");

        String accessToken= request.getHeader("access");

        String message = tokenCheck(accessToken, "access");

        if(!message.isEmpty()){
            return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.getEmail(accessToken);

        UserEntity userEntity = userRepository.findByEmail(email);

        UserDTO userDTO = UserDTO.ToUserDTO(userEntity);

        userDTO.setPassword("");

        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    public String tokenCheck(String token,String TokenCategory){

        if(token == null){

            return "토큰이 존재하지 않습니다";
        }

        try {
            jwtUtil.isExpired(token);
        }catch (ExpiredJwtException e){
            //응답 코드 설정
            return "토큰이 만료되었습니다.";
        }

        String category = jwtUtil.getCategory(token);

        if(!category.equals(TokenCategory)){

            //응답 코드 설정
            return "토큰이 유효하지 않습니다.";
        }

        if(TokenCategory.equals("refresh")) {
            Boolean isExist = refreshRepository.existsByRefresh(token);

            if (!isExist) {

                return "저장된 토큰이 존재하지 않습니다.";
            }
        }
        return "";
    }
}
