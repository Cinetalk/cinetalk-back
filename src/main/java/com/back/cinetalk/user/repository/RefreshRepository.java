package com.back.cinetalk.user.repository;

import com.back.cinetalk.user.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefresh(String refresh);

    Boolean existsByAuth(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);

    RefreshEntity findByAuth(String auth);
}
