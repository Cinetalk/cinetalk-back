package com.back.cinetalk.badge.repository;

import com.back.cinetalk.badge.entity.BadgeEntity;
import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.genre.entity.GenreEntity;
import com.back.cinetalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {

    Optional<BadgeEntity> findByGenre(GenreEntity genre);
}
