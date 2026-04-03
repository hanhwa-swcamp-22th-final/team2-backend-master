package com.team2.master.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Master Service API")
                        .version("1.0")
                        .description("거래처, 바이어, 품목, 국가, 통화, 인코텀즈, 결제조건, 항구 관리 API"));
    }
}
