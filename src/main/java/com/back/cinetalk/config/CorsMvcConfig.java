package com.back.cinetalk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
// cicd test 중 test 1
@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie","access","refresh")
                //.allowedOrigins("http://localhost:3000")
                .allowedOriginPatterns("*") // cors 모든 허용
                .allowedMethods("*")
                .allowCredentials(true);
    }
}
