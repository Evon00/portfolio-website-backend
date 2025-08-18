package com.example.portfolio_website_backend.common.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT token을 입력하세요"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {


        return new OpenAPI()
                .components(getComponents())
                .info(apiInfo());
    }

    private Components getComponents() {
        return new Components()
                .addSecuritySchemes("Authorization",
                        new io.swagger.v3.oas.models.security.SecurityScheme()
                                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Authorization header using the Bearer scheme. Example: 'Authorization: Bearer {token}'"));
    }

    private Info apiInfo() {
        return new Info()
                .title("JH's Portfolio API")
                .version("1.0.0")
                .description("포트폴리오 프로젝트의 REST API 문서입니다.");
    }
}
