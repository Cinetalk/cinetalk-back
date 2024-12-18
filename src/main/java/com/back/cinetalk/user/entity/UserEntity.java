package com.back.cinetalk.user.entity;

import com.back.cinetalk.bookmark.entity.BookmarkEntity;
import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.damage.entity.DamageEntity;
import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.keyword.entity.UserKeywordClickEntity;
import com.back.cinetalk.rate.dislike.entity.ReviewDislikeEntity;
import com.back.cinetalk.rate.like.entity.ReviewLikeEntity;
import com.back.cinetalk.report.entity.ReportEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.dto.NickNameMergeDTO;
import com.back.cinetalk.user.dto.UserDTO;
import com.back.cinetalk.userBadge.entity.UserBadgeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity
@Getter
@Table(name = "User")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String nickname;

    private String gender;

    private LocalDate birthday;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] profile;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] profile_hd;

    private String provider;

    private String role;

    @OneToMany(mappedBy = "user")
    private List<ReviewEntity> reviewEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<KeywordEntity> keywordEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BookmarkEntity> bookmarkEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserBadgeEntity> userBadgeEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ReviewLikeEntity> reviewLikeEntityList = new ArrayList<>(); // 좋아요 리스트

    @OneToMany(mappedBy = "user")
    private List<ReviewDislikeEntity> reviewDislikeEntityList = new ArrayList<>(); // 싫어요 리스트

    @OneToMany(mappedBy = "user")
    private List<ReportEntity> reportEntityList = new ArrayList<>(); // 신고 리스트

    @OneToMany(mappedBy = "user")
    private List<DamageEntity> damageEntityList = new ArrayList<>(); // 제재 리스트

    @OneToMany(mappedBy = "user")
    private List<UserKeywordClickEntity> userKeywordClickEntityList = new ArrayList<>(); // 제재 리스트

    public static UserEntity ToUserEntity(UserDTO userDTO) {
        return UserEntity.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                //.name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .gender(userDTO.getGender())
                .birthday(userDTO.getBirthday())
                .profile(TodecodeProfile(userDTO.getProfile()))
                .provider(userDTO.getProvider())
                .role(userDTO.getRole())
                .build();
    }

    public static byte[] TodecodeProfile(String profileString) {
        if (profileString != null) {
            return Base64.getDecoder().decode(profileString);
        } else {
            return null;
        }
    }

    public void Update(NickNameMergeDTO dto) {
        this.nickname = dto.getNickname();
        this.gender = dto.getGender();
        this.birthday = dto.getBirthday();
    }

    public void UpdateProfile(byte[] profile,byte[] profile_hd) {
        this.profile = profile;
        this.profile_hd = profile_hd;
    }

    public void DeleteUser(String email,String nickname){
        this.email = email;
        this.nickname = nickname;
    }
}
