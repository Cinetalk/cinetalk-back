package com.back.cinetalk.keyword.entity;

import com.back.cinetalk.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "User_Keyword_Click")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserKeywordClickEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private KeywordEntity keyword;

    public static UserKeywordClickEntity toUserKeywordClickEntity(UserEntity user, KeywordEntity keyword) {
        return UserKeywordClickEntity.builder()
                .user(user)
                .keyword(keyword)
                .build();
    }

}
