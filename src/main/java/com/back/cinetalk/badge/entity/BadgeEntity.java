package com.back.cinetalk.badge.entity;

import com.back.cinetalk.genre.entity.GenreEntity;
import com.back.cinetalk.userBadge.entity.UserBadgeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "Badge")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;

    @OneToMany(mappedBy = "badge",cascade = CascadeType.ALL)
    private List<UserBadgeEntity> userBadgeEntityList;

}
