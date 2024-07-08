package com.back.cinetalk.user.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NicknameGenerator {
    private static NicknameGenerator instance;
    private Random rand;
    private String[] firstName;
    private String[] secondName;

    private NicknameGenerator() {
        rand = new Random();
        firstName = new String[]{"멋있는", "예쁜", "화끈한", "멍청한", "밝은", "섹시한", "우아한", "유쾌한", "명랑한", "힙한"};
        secondName = new String[]{"고양이", "강아지", "거북이", "토끼", "뱀", "사자", "호랑이", "표범", "치타", "기린", "코끼리", "코뿔소", "하마", "악어",
                "펭귄", "부엉이", "올빼미", "곰", "돼지", "소", "닭", "독수리", "타조" ,"개발자"};
    }

    public static NicknameGenerator getInstance() {
        if (instance == null) {
            synchronized (NicknameGenerator.class) {
                if (instance == null) {
                    instance = new NicknameGenerator();
                }
            }
        }
        return instance;
    }

    public String getNickname() {
        int firstIndex = rand.nextInt(firstName.length);
        int secondIndex = rand.nextInt(secondName.length);
        int number = rand.nextInt(10000);
        StringBuilder nickname = new StringBuilder();
        nickname.append(firstName[firstIndex]);
        nickname.append(secondName[secondIndex]);
        nickname.append(number);
        return nickname.toString();
    }
}
