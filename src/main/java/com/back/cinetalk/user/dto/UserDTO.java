package com.back.cinetalk.user.dto;

import com.back.cinetalk.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    private String email;

    private String password;

    private String name;

    private String nickname;

    private String provider;

    private String role;


    public static UserDTO ToUserDTO(UserEntity userEntity){
        return UserDTO.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .provider(userEntity.getProvider())
                .role(userEntity.getRole())
                .build();
    }
}
