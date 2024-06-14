package com.back.cinetalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
// @EnableJpaAuditing Spring boot 에서 이 어노테이션을 지원함
public class CinetalkApplication{

    public static void main(String[] args) {
        SpringApplication.run(CinetalkApplication.class, args);
    }

}
