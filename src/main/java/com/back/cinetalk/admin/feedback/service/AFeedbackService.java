package com.back.cinetalk.admin.feedback.service;

import com.back.cinetalk.admin.feedback.dto.FeedBackListDTO;
import com.back.cinetalk.feedback.entity.QFeedbackEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AFeedbackService {

    private final JPAQueryFactory queryFactory;

    QFeedbackEntity feedback = QFeedbackEntity.feedbackEntity;

    public ResponseEntity<?> feedBackList(){

        List<FeedBackListDTO> resultList = queryFactory.select(Projections.constructor(
                        FeedBackListDTO.class,
                        feedback.id,
                        feedback.user.email.as("user_email"),
                        feedback.user.nickname.as("user_nickName"),
                        feedback.content
                ))
                .from(feedback)
                .orderBy(feedback.createdAt.desc())
                .fetch();


        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }
}
