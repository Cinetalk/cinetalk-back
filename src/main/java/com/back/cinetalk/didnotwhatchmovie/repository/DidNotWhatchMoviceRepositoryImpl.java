package com.back.cinetalk.didnotwhatchmovie.repository;

import com.back.cinetalk.didnotwhatchmovie.entity.DidNotWhatchMovieEntity;
import com.back.cinetalk.didnotwhatchmovie.entity.QDidNotWhatchMovieEntity;
import com.back.cinetalk.user.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class DidNotWhatchMoviceRepositoryImpl implements DidNotWhatchMovieRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public DidNotWhatchMoviceRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }
    @Override
    public List<DidNotWhatchMovieEntity> findCustomByUserId(Long userId) {
        // 사용자 정의 쿼리를 작성합니다.
        QDidNotWhatchMovieEntity qDidNotWhatchMovie = QDidNotWhatchMovieEntity.didNotWhatchMovieEntity;
        return query.selectFrom(qDidNotWhatchMovie)
                .where(qDidNotWhatchMovie.user.id.eq(userId))
                .fetch();
    }


}
