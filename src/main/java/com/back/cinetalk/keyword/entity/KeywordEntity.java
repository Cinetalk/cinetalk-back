package com.back.cinetalk.keyword.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Keyword")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String keyword;

    private int count;
}
