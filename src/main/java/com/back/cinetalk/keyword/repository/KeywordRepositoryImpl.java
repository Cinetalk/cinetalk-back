package com.back.cinetalk.keyword.repository;

import com.back.cinetalk.keyword.dto.KeywordResponseDTO;
import com.back.cinetalk.keyword.entity.KeywordEntity;
import com.back.cinetalk.keyword.entity.QKeywordEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class KeywordRepositoryImpl implements KeywordRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<KeywordResponseDTO> findKeywordsOrderByCountDesc(Long movieId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QKeywordEntity keywordEntity = QKeywordEntity.keywordEntity;

        return queryFactory
                .select(Projections.fields(KeywordResponseDTO.class,
                        keywordEntity.keyword,
                        keywordEntity.count.sum().as("count")))
                .from(keywordEntity)
                .where(keywordEntity.movieId.eq(movieId))
                .groupBy(keywordEntity.keyword)
                .orderBy(keywordEntity.count.sum().desc())
                .limit(26)
                .fetch();
    }
}
