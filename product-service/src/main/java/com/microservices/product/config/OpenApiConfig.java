package com.microservices.product.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("Product Service API")
                .description("Rest API Documentation For Product Service")
                .version("V0.0.1")
                .license(new License().name("Apache 3.5")))
                .externalDocs(new ExternalDocumentation()
                        .description("There is also an external Documentation available")
                        .url("https://product-service.com/docs"));
    }
}
