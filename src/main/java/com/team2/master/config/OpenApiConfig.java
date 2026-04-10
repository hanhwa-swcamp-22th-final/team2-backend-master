package com.team2.master.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Master Service API")
                        .version("1.0")
                        .description("거래처, 바이어, 품목, 국가, 통화, 인코텀즈, 결제조건, 항구 관리 API\n\n"
                                + "### Swagger 인증 방법\n"
                                + "Auth 서비스 (`localhost:8011`)에서 로그인 → `accessToken` 복사 → **Authorize** 🔓 클릭 → 붙여넣기"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                                .name(BEARER_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Access Token (RS256)")));
    }
}
