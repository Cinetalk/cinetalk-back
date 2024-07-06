package com.back.cinetalk.user.entity;

import com.back.cinetalk.user.dto.RefreshDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refresh",timeToLive = 86400L)
public class RefreshEntity{

    @Id
    @Indexed
    public String refresh;
    @Indexed
    public String email;
    public String ip;
    @Indexed
    public String access;
    public String expiration;
    @Indexed
    public String auth;

    public static RefreshEntity ToRefreshEntity(RefreshDTO refreshDTO){
        return RefreshEntity.builder()
                .refresh(refreshDTO.getRefresh())
                .email(refreshDTO.getEmail())
                .ip(refreshDTO.getIp())
                .access(refreshDTO.getAccess())
                .expiration(refreshDTO.getExpiration())
                .auth(refreshDTO.getAuth())
                .build();
    }
}
