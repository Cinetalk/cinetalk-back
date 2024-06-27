package com.back.cinetalk.user.dto;

import com.back.cinetalk.user.entity.RefreshEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshDTO {

    public Long id;
    public String email;
    public String ip;
    public String refresh;
    public String access;
    public String expiration;
    private String auth;

    public static RefreshDTO ToRefreshDTO(RefreshEntity refreshEntity){
        return RefreshDTO.builder()
                .id(refreshEntity.getId())
                .email(refreshEntity.getEmail())
                .ip(refreshEntity.getIp())
                .refresh(refreshEntity.getRefresh())
                .access(refreshEntity.getAccess())
                .expiration(refreshEntity.getExpiration())
                .auth(refreshEntity.getAuth())
                .build();
    }
}
