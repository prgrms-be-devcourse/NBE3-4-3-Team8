package com.ll.nbe342team8.global.springDoc;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

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
    public GroupedOpenApi deliveryInformationAPI() {
        return GroupedOpenApi.builder()
                .group("deliveryInformation")
                .pathsToMatch("/my/deliveryInformation/**")
                .build();
    }

    @Bean
    public GroupedOpenApi QuestionAnswerAPI() {
        return GroupedOpenApi.builder()
                .group("Q&A")
                .pathsToMatch("/my/question/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberAPI() {
        return GroupedOpenApi.builder()
                .group("member")
                .pathsToMatch("/my/**")
                .pathsToExclude("/my/deliveryInformation/**", "/my/question/**")  // 특정 경로 제외
                .build();
    }
	@Bean
	public GroupedOpenApi cartsAPI() {
		return GroupedOpenApi.builder()
				.group("cart")
				.pathsToMatch("/cart/**")
				.build();
	}

	@Bean
	public GroupedOpenApi ordersAPI() {
		return GroupedOpenApi.builder()
				.group("order")
				.pathsToMatch("/my/orders/**")
				.build();
	}

	@Bean
	public GroupedOpenApi adminAPI() {
		return GroupedOpenApi.builder()
				.group("admin")
				.pathsToMatch("/admin/**")
				.build();
	}
}

