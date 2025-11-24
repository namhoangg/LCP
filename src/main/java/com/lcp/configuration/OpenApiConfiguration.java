package com.lcp.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .securitySchemes(Map.of(
                                "bearerAuth",
                                new SecurityScheme()
                                        .in(SecurityScheme.In.HEADER)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )))
                .security(List.of(
                        new SecurityRequirement()
                                .addList("bearerAuth")
                ))
                .info(new Info()
                        .title("API")
                        .description("API document"));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
