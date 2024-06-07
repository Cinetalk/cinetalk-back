package com.back.cinetalk.review.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.review.dto.ReviewRequestDTO;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Review")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id")
    private Long movieId;

    // id가 있는데 movieName 필요 없을거같음

    @Column(name = "user_id")
    private Long userId;

    private Double star;

    private String content;

}
