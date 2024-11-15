package com.back.cinetalk.user.dto;

import com.back.cinetalk.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Base64;

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

    private String profile;

    private String provider;

    private String role;


    public static UserDTO ToUserDTO(UserEntity userEntity){

        String profileBase64 = null;
        byte[] profileBytes = userEntity.getProfile_hd();
        if (profileBytes != null) {
            profileBase64 = Base64.getEncoder().encodeToString(profileBytes);
        }

        return UserDTO.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                //.name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .gender(userEntity.getGender())
                .birthday(userEntity.getBirthday())
                .profile(profileBase64)
                .provider(userEntity.getProvider())
                .role(userEntity.getRole())
                .build();
    }
}
