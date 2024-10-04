package com.back.cinetalk.user.service;

import com.back.cinetalk.user.dto.*;
import com.back.cinetalk.user.repository.UserRepository;
import com.back.cinetalk.user.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;


    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)throws OAuth2AuthenticationException{

        //부모 클래스에서 request 값을 꺼내오는 처리
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("oAuth2User :"+oAuth2User);

        //어떤 소셜 로그인인지 판단
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        log.info("registrationId :"+registrationId);

        //네이버 로그인일때
        if(registrationId.equals("naver")){

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        //구글 로그인일때
        else if(registrationId.equals("google")){

            String accessToken = userRequest.getAccessToken().getTokenValue();

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes(),accessToken);

            log.info(String.valueOf("oAuth2Response : "+oAuth2Response));
        }

        else if(registrationId.equals("kakao")){

            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }

        else{

            return null;
        }

        //DB에 저장되어있는 아이디 인지 확인
        UserEntity existData = userRepository.findByEmail(oAuth2Response.getEmail());

        UserDTO userDTO = new UserDTO();

        userDTO.setEmail(oAuth2Response.getEmail());
        //oauth 디폴트 password 생성
        userDTO.setPassword("oauthdefalt");
        //이름 받아오기 금지
        //userDTO.setName(oAuth2Response.getName());
        userDTO.setGender(oAuth2Response.getGender());
        userDTO.setBirthday(oAuth2Response.getBirthday());
        //프로필 받아오기 금지
        //userDTO.setProfile(Base64.getEncoder().encodeToString(oAuth2Response.getProfile()));
        userDTO.setProfile(null);
        userDTO.setProvider(oAuth2Response.getProvider());

        //DB에 존재하지 않을 경우 : 회원가입
        if(existData == null){

            userDTO.setRole("ROLE_USER");

            UserEntity userEntity = UserEntity.ToUserEntity(userDTO);

            userRepository.save(userEntity);

            return new CustomOAuth2User(userDTO);

        }
        //DB에 존재할 경우 : 로그인  --- ※다른 소셜에 이미 있는 이메일일 경우 처리 필요
        else{

            userDTO.setRole(existData.getRole());
            
            //같은 소셜 로그인일 경우
            if(oAuth2Response.getProvider().equals(existData.getProvider())){


                return new CustomOAuth2User(userDTO);
            }
            //다른 소셜 로그인일 경우
            else {

                userRepository.updateProviderByEmail(oAuth2Response.getProvider(),oAuth2Response.getEmail());

                return new CustomOAuth2User(userDTO);
            }
        }
    }
}
