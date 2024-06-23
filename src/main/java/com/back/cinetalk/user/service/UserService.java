package com.back.cinetalk.user.service;

import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.entity.RefreshEntity;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.RefreshRepository;
import com.back.cinetalk.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
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

    private final UserRepository userRepository;

    public ResponseEntity<?> UserInfo(HttpServletRequest request, HttpServletResponse response){

        log.info("User정보 로직");

        String accessToken= request.getHeader("access");

        String email = jwtUtil.getEmail(accessToken);

        UserEntity userEntity = userRepository.findByEmail(email);

        UserDTO userDTO = UserDTO.ToUserDTO(userEntity);

        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    public ResponseEntity<?> nickNameMerge(HttpServletRequest request,UserDTO userDTO){

        log.info("닉네임 재설정 로직");

        Boolean nickYN = userRepository.existsByNickname(userDTO.getNickname());

        if(nickYN){
            return new ResponseEntity<>("already nickname",HttpStatus.OK);
        }

        String accessToken= request.getHeader("access");

        String email = jwtUtil.getEmail(accessToken);

        //userRepository.updateNicknameByEmail(email,nickname);

        return new ResponseEntity<>("success",HttpStatus.OK);
    }
}
