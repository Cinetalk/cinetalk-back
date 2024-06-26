package com.back.cinetalk.user.service;

import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.repository.RefreshRepository;
import com.back.cinetalk.user.dto.RefreshDTO;
import com.back.cinetalk.user.entity.RefreshEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
public class ReIssueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;

    public ReIssueService(JWTUtil jwtUtil, RefreshRepository refreshRepository, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        log.info("access 토큰 재생성 로직 호출");
        //refresh 토큰 변수 설정
        String refresh = null;

        Cookie[] cookies = request.getCookies();

        log.info(Arrays.toString(cookies));

        for (Cookie cookie : cookies){
            log.info(cookie.getName());
            if (cookie.getName().equals("refresh")){

                refresh = cookie.getValue();
            }
        }

        log.info("refresh token : "+refresh);

        //쿠키배열에 토큰이 존재하지 않을때
        if (refresh == null){

            //응답 코드 설정
            return new ResponseEntity<>("refresh토큰이 존재하지 않습니다", HttpStatus.BAD_REQUEST);
        }

        //토큰 만료 체크
        try {
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){

            //응답 코드 설정
            return new ResponseEntity<>("refresh토큰이 만료되었습니다.", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory(refresh);

        if(!category.equals("refresh")){

            //응답 코드 설정
            return new ResponseEntity<>("refresh토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);

        if(!isExist){

            //응답 코드 설정
            return new ResponseEntity<>("저장된 토큰이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);

        //access 토큰 재생성
        String newAccess = jwtUtil.createJwt("access",email,role,600000L);
        //refresh 토큰 재생성
        String newRefresh = jwtUtil.createJwt("refresh",email,role,86400000L);

        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(email,newRefresh,86400000L);

        response.setHeader("access",newAccess);
        response.addCookie(createCookie("refresh",newRefresh));
        log.info("토큰 재생성 성공");

        //응답 바디
        //PrintWriter writer = response.getWriter();
        //writer.print("token reissue");

        UserEntity userEntity = userRepository.findByEmail(email);

        return new ResponseEntity<>(UserDTO.ToUserDTO(userEntity),HttpStatus.OK);
    }

    private Cookie createCookie(String key,String value){

        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String email,String refresh,Long expiredMs){

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshDTO refreshDTO = new RefreshDTO();
        refreshDTO.setEmail(email);
        refreshDTO.setRefresh(refresh);
        refreshDTO.setExpiration(date.toString());

        RefreshEntity refreshEntity = RefreshEntity.ToRefreshEntity(refreshDTO);

        refreshRepository.save(refreshEntity);
    }

    public ResponseEntity<?> AuthBy(HttpServletResponse response,String auth){

        RefreshEntity byAuth = refreshRepository.findByAuth(auth);

        refreshRepository.updateAuthToNullByAuth(auth);

        if(byAuth == null){

            return new ResponseEntity<>("token null",HttpStatus.OK);
        }

        String email = jwtUtil.getEmail(byAuth.getRefresh());
        String role = jwtUtil.getRole(byAuth.getRefresh());

        //access 토큰 재생성
        String newAccess = jwtUtil.createJwt("access",email,role,600000L);

        UserEntity userEntity = userRepository.findByEmail(email);

        response.setHeader("access",newAccess);
        response.addCookie(createCookie("refresh",byAuth.getRefresh()));

        return new ResponseEntity<>(UserDTO.ToUserDTO(userEntity),HttpStatus.OK);
    }
}
