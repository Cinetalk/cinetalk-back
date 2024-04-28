package com.back.cinetalk.user.entity;

import com.back.cinetalk.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "User")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String nickname;

    private String provider;

    private String role;

    public static UserEntity ToUserEntity(UserDTO userDTO){
        return UserEntity.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .provider(userDTO.getProvider())
                .role(userDTO.getRole())
                .build();
    }
}
