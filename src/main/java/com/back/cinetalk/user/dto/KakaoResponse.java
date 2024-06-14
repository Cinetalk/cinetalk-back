package com.back.cinetalk.user.dto;

import java.time.LocalDate;
import java.util.Map;

public class KakaoResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("kakao_account");

        System.out.println(attribute);
    }

    @Override
    public String getProvider() {

        return "kakao";
    }

    @Override
    public String getProviderId() {

        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {

        return attribute.get("email").toString();
    }

    @Override
    public String getName() {

        Map<String,Object> profile = (Map<String, Object>) attribute.get("profile");

        return profile.get("nickname").toString();
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public LocalDate getBirthday() {
        return null;
    }

    @Override
    public byte[] getProfile() {
        return new byte[0];
    }
}
