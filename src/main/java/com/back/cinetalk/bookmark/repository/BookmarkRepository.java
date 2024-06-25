package com.back.cinetalk.bookmark.repository;

import com.back.cinetalk.bookmark.entity.BookmarkEntity;
import com.back.cinetalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    List<BookmarkEntity> findByUserOrderByCreatedAtDesc(UserEntity user);
}
