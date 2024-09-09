package com.back.cinetalk.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SpringTests {

    @Test
    public void test() {

        LocalDateTime sixAgo = LocalDate.now()
                .minusMonths(10)
                .withDayOfMonth(1)
                .atStartOfDay();

        String s = String.valueOf(sixAgo).substring(0,7);

        System.out.println(s);
    }
}
