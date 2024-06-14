package com.back.cinetalk.user.dto;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class GoogleResponse implements OAuth2Response{

    private final Map<String, Object> attribute;
    private final String accssToken;

    private String gender;
    private LocalDate birthday;

    public GoogleResponse(Map<String, Object> attribute,String accssToken) {
        this.attribute = attribute;
        this.accssToken = accssToken;
    }


    @Override
    public String getProvider() {

        return "google";
    }

    @Override
    public String getProviderId() {

        return attribute.get("sub").toString();
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

        return gender;
    }

    @Override
    public LocalDate getBirthday() {

        return birthday;
    }

    @Override
    public byte[] getProfile() {

        RestTemplate restTemplate = new RestTemplate();

        String url = attribute.get("picture").toString();

        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);

        if (response.getStatusCode().is2xxSuccessful()) {

            return response.getBody();
        } else {

            return null;
        }
    }
    private void fetchAdditionalUserInfo() {
        String url = "https://people.googleapis.com/v1/people/me?personFields=genders,birthdays";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accssToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> profile = response.getBody();

        if (profile != null) {
            List<Map<String, Object>> genders = (List<Map<String, Object>>) profile.get("genders");
            List<Map<String, Object>> birthdays = (List<Map<String, Object>>) profile.get("birthdays");

            if (genders != null && !genders.isEmpty()) {
                gender = (String) genders.get(0).get("value");
            }

            if (birthdays != null && !birthdays.isEmpty()) {
                Map<String, Object> date = (Map<String, Object>) birthdays.get(0).get("date");
                if (date != null) {
                    int year = (int) date.get("year");
                    int month = (int) date.get("month");
                    int day = (int) date.get("day");
                    birthday = LocalDate.of(year, month, day);
                }
            }
        }
    }

    public Map<String, Object> getAttributes() {
        return attribute;
    }
}
