package com.back.cinetalk.user.entity;

import com.back.cinetalk.bookmark.entity.BookmarkEntity;
import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.rate.entity.RateEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private String gender;

    private LocalDate birthday;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profile;

    private String provider;

    private String role;

    @OneToMany(mappedBy = "user")
    private List<ReviewEntity> reviewEntityList = new ArrayList<ReviewEntity>();

    @OneToMany(mappedBy = "user")
    private List<KeywordEntity> keywordEntityList = new ArrayList<KeywordEntity>();

    @OneToMany(mappedBy = "user")
    private List<RateEntity> rateEntityList = new ArrayList<RateEntity>();

    @OneToMany(mappedBy = "user")
    private List<BookmarkEntity> bookmarkEntityList = new ArrayList<BookmarkEntity>();

    public static UserEntity ToUserEntity(UserDTO userDTO){
        return UserEntity.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .gender(userDTO.getGender())
                .birthday(userDTO.getBirthday())
                .profile(userDTO.getProfile())
                .provider(userDTO.getProvider())
                .role(userDTO.getRole())
                .build();
    }
}
