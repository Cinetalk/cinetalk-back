package com.back.cinetalk.user.dto;

import com.back.cinetalk.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "User 데이터 담기는 DTO")
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

    private String gender;

    private LocalDate birthday;

    private byte[] profile;

    private String provider;

    private String role;

    private List<String> favoriteGenres;


    public static UserDTO ToUserDTO(UserEntity userEntity){
        return UserDTO.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .gender(userEntity.getGender())
                .birthday(userEntity.getBirthday())
                .profile(userEntity.getProfile())
                .provider(userEntity.getProvider())
                .role(userEntity.getRole())
                .build();
    }
}
