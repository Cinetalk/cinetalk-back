package com.back.cinetalk.badge.repository;

import com.back.cinetalk.badge.entity.BadgeEntity;
import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {

    List<BadgeEntity> findByUser(UserEntity user);
}
