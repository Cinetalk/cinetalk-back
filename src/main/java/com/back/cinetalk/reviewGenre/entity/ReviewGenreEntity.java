package com.back.cinetalk.reviewGenre.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.genre.entity.GenreEntity;
import com.back.cinetalk.review.entity.ReviewEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Review_Genre")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewGenreEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;
}
