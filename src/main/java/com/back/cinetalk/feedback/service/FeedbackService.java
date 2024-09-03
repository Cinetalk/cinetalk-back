package com.back.cinetalk.feedback.service;

import com.back.cinetalk.config.dto.StateRes;
import com.back.cinetalk.exception.errorCode.CommonErrorCode;
import com.back.cinetalk.exception.exception.RestApiException;
import com.back.cinetalk.feedback.entity.FeedbackEntity;
import com.back.cinetalk.feedback.repository.FeedbackRepository;
import com.back.cinetalk.user.MyPage.component.UserByAccess;
import com.back.cinetalk.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UserByAccess userByAccess;
    private final FeedbackRepository feedbackRepository;

    public StateRes FeedbackSave(HttpServletRequest request,String content){

        UserEntity userEntity = userByAccess.getUserEntity(request);

        if(content == null || content.length() > 200 || content.replaceAll(" ","").isEmpty()){

            throw new RestApiException(CommonErrorCode.FEEDBACK_CONTENT_WRONG);
        }

        feedbackRepository.save(FeedbackEntity.builder()
                .user(userEntity)
                .content(content)
                .build());

        return new StateRes(true);
    }
}
