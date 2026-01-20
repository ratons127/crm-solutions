package com.betopia.hrm.webapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${app.protocol}")
    private String protocol;

    @Value("${app.domain}")
    private String domain;

    @Bean
    public OpenAPI openAPI()
    {
        String serverUrl = String.format("%s://%s", protocol, domain);

        return new OpenAPI()
                .info(new Info()
                        .title("A Betopia Group Human Resource Management System")
                        .version("1.0")
                        .description("This is the API documentation for betopia-hrm.com"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .servers(List.of(new Server().url(serverUrl)));
    }
}
