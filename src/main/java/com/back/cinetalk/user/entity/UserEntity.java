package com.back.cinetalk.user.entity;

import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.rereview.entity.ReReviewEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user")
    private List<ReviewEntity> reviewEntityList = new ArrayList<ReviewEntity>();

    @OneToMany(mappedBy = "user")
    private List<ReReviewEntity> reReviewEntityList = new ArrayList<ReReviewEntity>();

    @OneToMany(mappedBy = "user")
    private List<KeywordEntity> keywordEntityList = new ArrayList<KeywordEntity>();

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
