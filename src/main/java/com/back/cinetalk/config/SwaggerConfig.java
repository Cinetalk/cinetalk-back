package com.back.cinetalk.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@OpenAPIDefinition(servers = {
        @Server(url = "https://cinetalk.kro.kr", description = "https"),
        @Server(url = "http://localhost:80", description = "local")
})
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("access");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("AccessToken");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("AccessToken", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }

    private Info apiInfo() {

        return new Info()

                .title("Cinetalk-Server API 명세서")
                .description("")
                .version("1.0.0");
    }
}
