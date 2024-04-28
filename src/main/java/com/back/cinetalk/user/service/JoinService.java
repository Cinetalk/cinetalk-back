package com.back.cinetalk.user.service;

import com.back.cinetalk.user.repository.UserRepository;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.user.dto.UserDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(UserDTO UserDTO) {

        String email = UserDTO.getEmail();
        String password = UserDTO.getPassword();

        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {

            return;
        }

        UserDTO.setPassword(bCryptPasswordEncoder.encode(password));
        UserDTO.setRole("ROLE_USER");

        UserEntity userEntity = UserEntity.ToUserEntity(UserDTO);

        userRepository.save(userEntity);
    }
}
