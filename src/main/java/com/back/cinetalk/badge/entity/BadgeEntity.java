package com.back.cinetalk.badge.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.genre.entity.GenreEntity;
import com.back.cinetalk.reviewGenre.entity.ReviewGenreEntity;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.userBadge.entity.UserBadgeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@Table(name = "Badge")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;

    @OneToMany(mappedBy = "badge")
    private List<UserBadgeEntity> userBadgeEntityList;
}
