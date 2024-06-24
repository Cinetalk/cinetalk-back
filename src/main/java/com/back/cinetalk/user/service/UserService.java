package com.back.cinetalk.user.service;

import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.user.MyPage.component.UserByAccess;
import com.back.cinetalk.user.dto.NickNameMergeDTO;
import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.jwt.JWTUtil;
import com.back.cinetalk.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    public final JWTUtil jwtUtil;

    private final UserRepository userRepository;

    private final UserByAccess userByAccess;

    @Transactional(readOnly = true)
    public ResponseEntity<?> UserInfo(HttpServletRequest request){

        log.info("User정보 로직");

        String accessToken= request.getHeader("access");

        String email = jwtUtil.getEmail(accessToken);

        UserEntity userEntity = userRepository.findByEmail(email);

        UserDTO userDTO = UserDTO.ToUserDTO(userEntity);

        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> nickNameMerge(HttpServletRequest request, NickNameMergeDTO dto){

        log.info("닉네임 재설정 로직");

        UserEntity userEntity = userByAccess.getUserEntity(request);

        Boolean nickYN = userRepository.existsByNickname(dto.getNickname());

        if(nickYN){
            throw new RestApiException(CommonErrorCode.NICKNAME_ALREADY_EXIST);
        }

        userEntity.update(dto);

        return new ResponseEntity<>("success",HttpStatus.OK);
    }
}
