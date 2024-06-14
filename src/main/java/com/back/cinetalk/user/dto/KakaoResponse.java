package com.back.cinetalk.user.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        return attribute.get("name").toString();
    }

    @Override
    public String getGender() {

        String gender = attribute.get("gender").toString();

        if(gender.equals("male")){
            return "M";
        }else{
            return "F";
        }
    }

    @Override
    public LocalDate getBirthday() {

        String birthyear = attribute.get("birthyear").toString();

        String birthday = attribute.get("birthday").toString();

        String birth = birthyear+"-"+birthday.substring(0,2)+"-"+birthday.substring(2);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return LocalDate.parse(birth, formatter);
    }

    @Override
    public byte[] getProfile() {

        RestTemplate restTemplate = new RestTemplate();

        Map<String,Object> profile = (Map<String, Object>) attribute.get("profile");

        String url = profile.get("profile_image_url").toString();

        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

        if (response.getStatusCode().is2xxSuccessful()) {

            return response.getBody();
        } else {

            return null;
        }
    }
}
