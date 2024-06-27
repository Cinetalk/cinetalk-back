package com.back.cinetalk.user.entity;

import com.back.cinetalk.config.entity.BaseEntity;
import com.back.cinetalk.user.dto.RefreshDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Refresh")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String email;
    public String ip;
    public String refresh;
    public String access;
    public String expiration;
    public String auth;

    public static RefreshEntity ToRefreshEntity(RefreshDTO refreshDTO){
        return RefreshEntity.builder()
                .id(refreshDTO.getId())
                .email(refreshDTO.getEmail())
                .ip(refreshDTO.getIp())
                .refresh(refreshDTO.getRefresh())
                .access(refreshDTO.getAccess())
                .expiration(refreshDTO.getExpiration())
                .auth(refreshDTO.getAuth())
                .build();
    }

    public void updateAccess(String access){ this.access = access;}
}
