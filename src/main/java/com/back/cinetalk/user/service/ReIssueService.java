package com.back.cinetalk.user.service;

import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
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
    private final ClientIpAddress clientIpAddress;

    public ReIssueService(JWTUtil jwtUtil, RefreshRepository refreshRepository, UserRepository userRepository, ClientIpAddress clientIpAddress) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.userRepository = userRepository;
        this.clientIpAddress = clientIpAddress;
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
            throw new RestApiException(CommonErrorCode.REFRESH_TOKEN_NULL);
        }

        //토큰 만료 체크
        try {
            jwtUtil.isExpired(refresh);
        }catch (ExpiredJwtException e){

            //응답 코드 설정
            throw new RestApiException(CommonErrorCode.REFRESH_TOKEN_ISEXPIRED);
        }

        String category = jwtUtil.getCategory(refresh);

        if(!category.equals("refresh")){

            //응답 코드 설정
            throw new RestApiException(CommonErrorCode.REFRESH_TOKEN_UNDIFINED);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);

        if(!isExist){

            //응답 코드 설정
            throw new RestApiException(CommonErrorCode.REFRESH_TOKEN_NOT_SAVED);
        }

        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);

        //access 토큰 재생성
        String newAccess = jwtUtil.createJwt("access",email,role,600000L);
        //refresh 토큰 재생성
        String newRefresh = jwtUtil.createJwt("refresh",email,role,2592000000L);

        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(email,newRefresh,2592000000L,request);

        response.setHeader("access",newAccess);
        response.addCookie(createCookie("refresh",newRefresh));
        log.info("토큰 재생성 성공");

        UserEntity userEntity = userRepository.findByEmail(email);

        return new ResponseEntity<>(UserDTO.ToUserDTO(userEntity),HttpStatus.OK);
    }

    private Cookie createCookie(String key,String value){

        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(24*60*60*30);
        cookie.setSecure(true);
        cookie.setPath("/"); //이거 안해 주면 시발 특정 경로에서 쿠키 보내야 받을수있음 시발
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite","None");
        return cookie;
    }

    private void addRefreshEntity(String email,String refresh,Long expiredMs,HttpServletRequest request){

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshDTO refreshDTO = new RefreshDTO();
        refreshDTO.setEmail(email);
        refreshDTO.setIp(clientIpAddress.getClientIp(request));
        refreshDTO.setRefresh(refresh);
        refreshDTO.setExpiration(date.toString());

        RefreshEntity refreshEntity = RefreshEntity.ToRefreshEntity(refreshDTO);

        refreshRepository.save(refreshEntity);
    }

    public ResponseEntity<?> AuthBy(HttpServletResponse response,String auth){

        RefreshEntity byAuth = refreshRepository.findByAuth(auth);

        if(byAuth == null){

            return new ResponseEntity<>("token null",HttpStatus.OK);
        }

        String email = jwtUtil.getEmail(byAuth.getRefresh());
        String role = jwtUtil.getRole(byAuth.getRefresh());

        //access 토큰 재생성
        String newAccess = jwtUtil.createJwt("access",email,role,600000L);

        UserEntity userEntity = userRepository.findByEmail(email);

        byAuth.setAuth(null);
        byAuth.setAccess(newAccess);

        refreshRepository.save(byAuth);

        response.setHeader("access",newAccess);
        response.addCookie(createCookie("refresh",byAuth.getRefresh()));

        return new ResponseEntity<>(UserDTO.ToUserDTO(userEntity),HttpStatus.OK);
    }
}
