package com.fintech.loanapi.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loanApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loan Origination & Management API")
                        .description("FinTech Core Banking Domain - DDD ve Hexagonal Architecture prensipleriyle geliştirilmiş kredi yönetim sistemi.")
                        .version("v1.0.0"));
    }
}