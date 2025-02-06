package com.ll.nbe342team8.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API 서버", version = "v1"))
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi booksAPI() {
        return GroupedOpenApi.builder()
                .group("book")
                .pathsToMatch("/books/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reviewsAPI() {
        return GroupedOpenApi.builder()
                .group("review")
                .pathsToMatch("/reviews/**")
                .build();
    }

    @Bean
    public GroupedOpenApi cartsAPI() {
        return GroupedOpenApi.builder()
                .group("cart")
                .pathsToMatch("/cart/**")
                .build();
    }
}
