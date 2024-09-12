package com.back.cinetalk.damage.repository;

import com.back.cinetalk.damage.entity.DamageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DamageRepository extends JpaRepository<DamageEntity,Long> {
}
