package com.back.cinetalk.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SpringTests {

    @Test
    public void test() {


        LocalDate date1 = LocalDate.of(2024, 9, 20);
        LocalDate date2 = LocalDate.of(2024, 10, 25);

        long daysBetween = ChronoUnit.DAYS.between(date1, date2)+1;

        System.out.println(daysBetween);
    }
}
