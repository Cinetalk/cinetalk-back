package com.back.cinetalk.user.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Map;

public class NaverResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {

        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {

        return "naver";
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
        //return attribute.get("name").toString();
        //이름을 빈값이나 null 대신 이걸로 받기로함
        return attribute.get("email").toString();
    }


    @Override
    public String getGender() {

        //return attribute.get("gender").toString();
        return "E";
    }

    @Override
    public LocalDate getBirthday() {

        //String birthString = attribute.get("birthyear").toString() + "-" + attribute.get("birthday").toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //return LocalDate.parse(birthString, formatter);
        return LocalDate.parse("2000-01-01", formatter);
    }

    /*
    @Override
    public byte[] getProfile() {

        RestTemplate restTemplate = new RestTemplate();

        String url = attribute.get("profile_image").toString();

        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

        if (response.getStatusCode().is2xxSuccessful()) {

            return response.getBody();
        } else {

            return null;
        }
    }

     */
}
