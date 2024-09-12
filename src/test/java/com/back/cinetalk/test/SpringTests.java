package com.back.cinetalk.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SpringTests {

    @Test
    public void test() {


        LocalDateTime time = LocalDateTime.now();
        LocalDate today = LocalDate.now();

        LocalDate day = today.minusDays(300);


        System.out.println(time);
        System.out.println(today);
        System.out.println(day);
    }
}
