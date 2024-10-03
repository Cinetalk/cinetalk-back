package com.back.cinetalk.damage.entity;

import com.back.cinetalk.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "Damage")
@Builder
@NoArgsConstructor
@AllArgsConstructor
//BasEntity 일부러 안해줌 : 필요없을 것 같아서..??
public class DamageEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //신고 사유 -- 신고 들어온것의 신고사유를 넣어주면 됨 front 가 해줄일
    private String category;

    //영화 이름 -- 제재했던 댓글의 영화 이름 (이정도면 걍 review_id 등록하면 되지않움? -> CascadeType.ALL)
    private String movienm;

    //댓글 내용 -- 제재를 가하게 되면 댓글은 삭제 되지만 댓글 내용은 보존해야 하기때문에
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    //제재 시작 날짜
    private LocalDate startDate;

    //제재 끝나는 날짜
    private LocalDate endDate;
}
