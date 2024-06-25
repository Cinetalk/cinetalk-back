package com.back.cinetalk;

import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.entity.QUserEntity;
import com.back.cinetalk.user.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslTest {


    @Autowired
    EntityManager em;

    @Test
    public void startQuerydsl() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUserEntity m = new QUserEntity("m");

        UserEntity findUser = queryFactory
                .select(m)
                .from(m)
                .where(m.name.eq("memeber1"))
                .fetchOne();
        assertThat(findUser.getName()).isEqualTo("memeber1");
    }

}
