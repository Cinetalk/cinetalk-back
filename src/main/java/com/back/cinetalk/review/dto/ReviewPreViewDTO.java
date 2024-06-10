package com.back.cinetalk.review.dto;

import com.back.cinetalk.review.entity.ReviewEntity;
import com.back.cinetalk.user.repository.UserRepository;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPreViewDTO {

    String nickName;

    Double star;

    String content;

    LocalDateTime createdAt;

    boolean spoiler;
}
