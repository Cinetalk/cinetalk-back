package com.back.cinetalk.userBadge.entity;

import com.back.cinetalk.badge.entity.BadgeEntity;
import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "User_Badge")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBadgeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private BadgeEntity badge;

    private boolean isUse;
}
