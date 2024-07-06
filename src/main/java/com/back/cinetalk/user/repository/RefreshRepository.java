package com.back.cinetalk.user.repository;

import com.back.cinetalk.user.entity.RefreshEntity;
import jakarta.validation.constraints.Null;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshRepository extends CrudRepository<RefreshEntity, Long> {

    Boolean existsByRefresh(String refresh);

    Boolean existsByAuth(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);

    RefreshEntity findByAuth(String auth);

    void deleteByEmail(String email);
}
