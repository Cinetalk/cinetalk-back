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
    public String refresh;
    public String expiration;

    public static RefreshDTO ToRefreshDTO(RefreshEntity refreshEntity){
        return RefreshDTO.builder()
                .id(refreshEntity.getId())
                .email(refreshEntity.getEmail())
                .refresh(refreshEntity.getRefresh())
                .expiration(refreshEntity.getExpiration())
                .build();
    }
}
