package com.back.cinetalk.user.oauth2;

import com.back.cinetalk.user.repository.RefreshRepository;
import com.back.cinetalk.user.dto.CustomOAuth2User;
import com.back.cinetalk.user.dto.RefreshDTO;
import com.back.cinetalk.user.entity.RefreshEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.UserRepository;
import com.back.cinetalk.user.service.AuthTokenCreate;
import com.back.cinetalk.user.service.ClientIpAddress;
import com.back.cinetalk.user.service.NicknameGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

@Component
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;
    private final NicknameGenerator nicknameGenerator;
    private final AuthTokenCreate authTokenCreate;
    private final ClientIpAddress clientIpAddress;

    public CustomSuccessHandler(JWTUtil jwtUtil, RefreshRepository refreshRepository, UserRepository userRepository, NicknameGenerator nicknameGenerator, AuthTokenCreate authTokenCreate, ClientIpAddress clientIpAddress) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.userRepository = userRepository;
        this.nicknameGenerator = nicknameGenerator;
        this.authTokenCreate = authTokenCreate;
        this.clientIpAddress = clientIpAddress;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String email = customUserDetails.getEmail();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //refresh 토큰생성 --리다이렉트 되면서 어처피 header 는 못받음
        //String access = jwtUtil.createJwt("access",email, role, 600000L);

        // 신규추가 240616 cross domain 을 위한 임시 토큰 생성
        String authToken = authTokenCreate.TokenCreate();

        String refresh = jwtUtil.createJwt("refresh",email, role, 2592000000L);

        //토큰 DB에 저장 --30일
        addRefreshEntity(email,refresh,2592000000L,authToken,request);

        String nickname = userRepository.findNicknameByEmail(email);

        //응답 설정
        //response.setHeader("access",access);
        response.addCookie(createCookie("refresh",refresh));
        response.setStatus(HttpStatus.OK.value());

        String redirectUrl = (String) request.getSession().getAttribute("redirecturl");

        //닉네임이 존재하지 않을 경우
        if(nickname == null){

            String Newnickname = nicknameGenerator.getNickname();

            userRepository.updateNicknameByEmail(email,Newnickname);

            byte[] profile = getRandomProfile();

            userRepository.updateProfileByEmail(email,profile);

            response.sendRedirect(redirectUrl+"/redirect/without-nickname?authToken="+authToken);
        }
        //닉네임이 존재할 경우
        else{

            response.sendRedirect(redirectUrl+"/redirect?authToken="+authToken);
        }
    }

    private Cookie createCookie(String key, String value){

        Cookie cookie = new Cookie(key,value);
        cookie.setMaxAge(24*60*60*30); //30일
        //https 만 쿠키전송
        cookie.setSecure(true);
        cookie.setPath("/"); //이거 안해 주면 시발 특정 경로에서 쿠키 보내야 받을수있음 시발
        cookie.setHttpOnly(false);
        cookie.setAttribute("SameSite","None");
        return cookie;
    }

    private void addRefreshEntity(String email,String refresh,Long expiredMs,String authToken,HttpServletRequest request){

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshDTO refreshDTO = new RefreshDTO();
        refreshDTO.setEmail(email);
        refreshDTO.setIp(clientIpAddress.getClientIp(request));
        refreshDTO.setRefresh(refresh);
        refreshDTO.setExpiration(date.toString());
        refreshDTO.setAuth(authToken);

        RefreshEntity refreshEntity = RefreshEntity.ToRefreshEntity(refreshDTO);

        refreshRepository.save(refreshEntity);
    }

    public byte[] getRandomProfile() throws IOException {

        String[] imageFiles = {
                "profile_blue.png",
                "profile_green.png",
                "profile_orange.png",
                "profile_red.png",
                "profile_violet.png"
        };

        Random random = new Random();
        int index = random.nextInt(imageFiles.length);
        String selectedImage = imageFiles[index];

        // 이미지 파일 로딩
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("profile_images/" + selectedImage)) {
            if (inputStream == null) {
                throw new IOException("Image not found: " + selectedImage);
            }

            // 이미지를 BufferedImage로 읽기
            BufferedImage image = ImageIO.read(inputStream);

            // 바이트 배열로 변환 (JPEG 형식으로 압축)
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // JPEGWriter와 압축 품질 설정
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
            jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(0.7f);  // 품질을 설정 (0.0f ~ 1.0f)

            writer.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
            writer.write(null, new javax.imageio.IIOImage(image, null, null), jpegParams);
            writer.dispose();

            // 압축된 이미지 바이트 배열 리턴
            return byteArrayOutputStream.toByteArray();
        }
    }
}
