package com.back.cinetalk.user.jwt;

import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.dto.CustomUserDetails;
import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.service.ClientIpAddress;
import com.nimbusds.jose.shaded.gson.JsonObject;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //응답 바디 한국어 설정
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        
        //access 토큰의 값을 요청 헤더에서 꺼내옴
        String accessToken= request.getHeader("access");

        log.info("accessToken : "+accessToken);

        //oauth 로그인 인지 판단
        String requestUri = request.getRequestURI();
        log.info("jwtfilter-uri : "+requestUri);

        //헤더가 없다면 다음 요청으로 넘김
        if (accessToken == null) {

            log.info("토큰 존재하지 않음");

            //다음 요청으로 넘기는 구문
            filterChain.doFilter(request, response);

            //메소드 종료
            return;
        }

        //토큰이 만료되었지만 로그인 url 일경우
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtUtil.isExpired(accessToken);
        }catch (ExpiredJwtException | MalformedJwtException e){
            //응답 바디
            PrintWriter writer = response.getWriter();
            response.setContentType("application/json");

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("error", "토큰이 만료되었거나 유효하지 않습니다.");

            // Gson 또는 JSON 라이브러리를 사용하여 JSON 문자열 생성
            String jsonString = jsonResponse.toString();
            writer.print(jsonString);

            //응답 코드 및 메소드 종료
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰이 access 토큰이 맞는지 확인
        String category = jwtUtil.getCategory(accessToken);

        if(!category.equals("access")){

            //응답 바디
            PrintWriter writer = response.getWriter();

            response.setContentType("application/json");

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("error", "유효하지 않는 토큰입니다.");

            // Gson 또는 JSON 라이브러리를 사용하여 JSON 문자열 생성
            String jsonString = jsonResponse.toString();
            writer.print(jsonString);

            //응답 코드 및 메소드 종료
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setPassword("temppassword");
        userDTO.setRole(role);

        UserEntity userEntity = UserEntity.ToUserEntity(userDTO);

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}