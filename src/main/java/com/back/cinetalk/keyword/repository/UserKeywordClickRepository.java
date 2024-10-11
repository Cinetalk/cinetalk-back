package com.back.cinetalk.keyword.repository;

import com.back.cinetalk.keyword.entity.UserKeywordClickEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserKeywordClickRepository extends JpaRepository<UserKeywordClickEntity, Long> {

    // 유저와 키워드를 기준으로 클릭 여부를 확인하는 메서드
    boolean existsByUserIdAndKeywordId(Long userId, Long keywordId);
}
