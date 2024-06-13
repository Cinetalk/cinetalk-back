package com.back.cinetalk.find.repository;

import com.back.cinetalk.find.entity.FindEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FindRepository extends JpaRepository<FindEntity, Long> {

}
