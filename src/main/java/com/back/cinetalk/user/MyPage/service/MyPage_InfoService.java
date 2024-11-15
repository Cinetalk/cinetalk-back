package com.back.cinetalk.user.MyPage.service;

import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.user.MyPage.component.UserByAccess;
import com.back.cinetalk.user.dto.NickNameMergeDTO;
import com.back.cinetalk.user.dto.ProfileDTO;
import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.repository.RefreshRepository;
import com.back.cinetalk.user.repository.UserRepository;
import com.back.cinetalk.user.service.ProfileCompress;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPage_InfoService {

    private final UserByAccess userByAccess;
    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;
    private final ProfileCompress profileCompress;

    //TODO 유저의 정보 
    @Transactional(readOnly = true)
    public ResponseEntity<?> UserInfo(HttpServletRequest request){

        log.info("User정보 로직");

        UserEntity userEntity = userByAccess.getUserEntity(request);

        UserDTO userDTO = UserDTO.ToUserDTO(userEntity);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    //TODO 닉네임 중복 체크
    @Transactional(readOnly = true)
    public Boolean NicknameCheck(String nickname){
        return userRepository.existsByNickname(nickname);
    }

    //TODO 회원 가입 시 닉네임,성별,생일 변경처리
    @Transactional
    public StateRes nickNameMerge(HttpServletRequest request, NickNameMergeDTO dto){

        log.info("닉네임 재설정 로직");

        UserEntity userEntity = userByAccess.getUserEntity(request);

        if(!dto.getNickname().equals(userEntity.getNickname())){

            Boolean nickYN = userRepository.existsByNickname(dto.getNickname());
            if(nickYN){
                throw new RestApiException(CommonErrorCode.NICKNAME_ALREADY_EXIST);
            }
        }
        userEntity.Update(dto);

        return new StateRes(true);
    }

    //TODO 마이 페이지 - 유저 정보 수정
    @Transactional
    public StateRes userInfoMerge(HttpServletRequest request,String category,String value){

        UserEntity userEntity = userByAccess.getUserEntity(request);

        NickNameMergeDTO dto = new NickNameMergeDTO();

        if(category.equals("nickname")){

            Boolean nickYN = userRepository.existsByNickname(value);

            if(nickYN){
                throw new RestApiException(CommonErrorCode.NICKNAME_ALREADY_EXIST);

            }
            dto = NickNameMergeDTO.builder().
                    nickname(value).
                    gender(userEntity.getGender()).
                    birthday(userEntity.getBirthday()).
                    build();

            userEntity.Update(dto);

        } else if (category.equals("gender")) {

            dto = NickNameMergeDTO.builder().
                    nickname(userEntity.getNickname()).
                    gender(value).
                    birthday(userEntity.getBirthday()).
                    build();

            userEntity.Update(dto);

        } else if (category.equals("birthday")) {

            dto = NickNameMergeDTO.builder().
                    nickname(userEntity.getNickname()).
                    gender(userEntity.getGender()).
                    birthday(LocalDate.parse(value)).
                    build();

            userEntity.Update(dto);
        }else{

            throw new RestApiException(CommonErrorCode.USER_CATEGORY_NOT_FOUND);
        }

        return new StateRes(true);
    }

    //TODO 유저의 프로필 사진 변경
    @Transactional
    public StateRes UserProfileChange(HttpServletRequest request,MultipartFile file){

        UserEntity userEntity = userByAccess.getUserEntity(request);

        long fileSize = file.getSize();

        long MB = 1024 * 1024;
        long maxFileSize = 1 * MB;

        ProfileDTO profileDTO = new ProfileDTO();
        try {
            if (maxFileSize > fileSize) {

                BufferedImage image = ImageIO.read(file.getInputStream());

                profileDTO = profileCompress.compressImage(image);
            } else {
                throw new RestApiException(CommonErrorCode.USER_IMAGE_MAX);
            }
        } catch (IOException e) {
            throw new RestApiException(CommonErrorCode.USER_IMAGE_ERROR);
        }

        userEntity.UpdateProfile(profileDTO.getProfile(),profileDTO.getProfile_hd());

        return new StateRes(true);
    }

    //TODO 회원 탈퇴
    @Transactional
    public StateRes UserDelete(HttpServletRequest request,HttpServletResponse response){

        UserEntity userEntity = userByAccess.getUserEntity(request);

        refreshRepository.deleteByEmail(userEntity.getEmail());

        userEntity.DeleteUser(null,"탈퇴한 유저");

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);

        return new StateRes(true);
    }
}
