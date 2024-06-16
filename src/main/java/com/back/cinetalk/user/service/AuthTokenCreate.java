package com.back.cinetalk.user.service;

import com.back.cinetalk.user.repository.RefreshRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AuthTokenCreate {

    private final RefreshRepository refreshRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 16;
    private static SecureRandom random = new SecureRandom();

    public AuthTokenCreate(RefreshRepository refreshRepository) {
        this.refreshRepository = refreshRepository;
    }

    public String TokenCreate(){

        String authtoken = "";

        while (true) {
            authtoken = randomCreate();

            if (!refreshRepository.existsByAuth(authtoken)) {
                return authtoken;
            }
        }
    }

    public String randomCreate() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            token.append(randomChar);
        }

        return token.toString();
    }

}
