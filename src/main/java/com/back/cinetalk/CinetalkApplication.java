package com.back.cinetalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
// 뭐가 문제일까
public class CinetalkApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(CinetalkApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(CinetalkApplication.class, args);
    }

}
