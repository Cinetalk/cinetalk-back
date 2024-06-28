package com.back.cinetalk.user.MyPage.service;

import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.user.MyPage.component.UserByAccess;
import com.back.cinetalk.user.dto.NickNameMergeDTO;
import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.entity.UserEntity;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
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
    public ResponseEntity<?> nickNameMerge(HttpServletRequest request, NickNameMergeDTO dto){

        log.info("닉네임 재설정 로직");

        UserEntity userEntity = userByAccess.getUserEntity(request);

        if(!dto.getNickname().equals(userEntity.getNickname())){

            Boolean nickYN = userRepository.existsByNickname(dto.getNickname());
            if(nickYN){
                throw new RestApiException(CommonErrorCode.NICKNAME_ALREADY_EXIST);
            }
        }
        userEntity.Update(dto);

        return new ResponseEntity<>("success",HttpStatus.OK);
    }

    //TODO 마이 페이지 - 유저 정보 수정
    @Transactional
    public ResponseEntity<?> userInfoMerge(HttpServletRequest request,String category,String value){

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

        return new ResponseEntity<>("success",HttpStatus.OK);
    }

    //TODO 유저의 프로필 사진 변경
    @Transactional
    public ResponseEntity<?> UserProfileChange(HttpServletRequest request,MultipartFile file){

        UserEntity userEntity = userByAccess.getUserEntity(request);

        long fileSize = file.getSize();

        long MB = 1024 * 1024;
        long maxFileSize = 1 * MB;

        byte[] imageBytes;
        try {
            if (maxFileSize < fileSize) {
                // 이미지 크기가 최대 크기를 초과하면 크기를 줄임
                BufferedImage originalImage = null;

                originalImage = ImageIO.read(file.getInputStream());

                // 이미지 리사이징
                Image resultingImage = originalImage.getScaledInstance(600, 800, Image.SCALE_DEFAULT);
                BufferedImage outputImage = new BufferedImage(600, 800, BufferedImage.TYPE_INT_RGB);
                outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(outputImage, "jpg", baos);
                imageBytes = baos.toByteArray();
            } else {
                imageBytes = file.getBytes();
            }
        } catch (IOException e) {
            return new ResponseEntity<>("이미지 등록 중 에러가 발생하였습니다.",HttpStatus.BAD_REQUEST);
        }

        userEntity.UpdateProfile(imageBytes);

        return new ResponseEntity<>("success",HttpStatus.OK);
    }

    //TODO 회원 탈퇴
    @Transactional
    public ResponseEntity<?> UserDelete(HttpServletRequest request,HttpServletResponse response){

        UserEntity userEntity = userByAccess.getUserEntity(request);

        refreshRepository.deleteByEmail(userEntity.getEmail());

        userRepository.deleteById(userEntity.getId());

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);

        return new ResponseEntity<>("success",HttpStatus.OK);
    }
}
