package com.back.cinetalk.user.repository;

import com.back.cinetalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Boolean existsByEmail(String email);

    UserEntity findByEmail(String email);

    @Query("SELECT u.nickname FROM UserEntity u WHERE u.email = :email")
    String findNicknameByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.nickname = :nickname WHERE u.email = :email")
    void updateNicknameByEmail(@Param("email") String email, @Param("nickname") String nickname);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.provider = :provider WHERE u.email = :email")
    void updateProviderByEmail(@Param("provider")String provider,@Param("email") String email);

}
