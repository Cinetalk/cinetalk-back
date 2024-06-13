package com.back.cinetalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CinetalkApplication{

    public static void main(String[] args) {
        SpringApplication.run(CinetalkApplication.class, args);
    }

}
