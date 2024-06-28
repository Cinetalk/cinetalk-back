package com.back.cinetalk.user.repository;

import com.back.cinetalk.user.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefresh(String refresh);

    Boolean existsByAuth(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);

    RefreshEntity findByAuth(String auth);

    void deleteByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE RefreshEntity e SET e.auth = NULL WHERE e.auth = :auth")
    void updateAuthToNullByAuth(@Param("auth") String auth);

    @Modifying
    @Transactional
    @Query("UPDATE RefreshEntity e SET e.access = :access WHERE e.id = :id")
    void updateAccessById(@Param("access") String access,@Param("id") Long id);

}
