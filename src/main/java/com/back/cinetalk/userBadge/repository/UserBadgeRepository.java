package com.back.cinetalk.userBadge.repository;

import com.back.cinetalk.badge.entity.BadgeEntity;
import com.back.cinetalk.user.entity.UserEntity;
import com.back.cinetalk.userBadge.entity.UserBadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadgeEntity, Long> {

    Optional<UserBadgeEntity> findByUserAndBadge(UserEntity user, BadgeEntity badge);
}
