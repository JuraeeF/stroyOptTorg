package com.example.stroy.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "BearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("СтройОптТорг Backend REST API")
                        .version("1.0.0")
                        .description("Ulgurji qurilish materiallari savdo platformasi uchun REST API hujjatlari. "
                                + "Avtorizatsiyadan o'tish uchun /api/auth/login orqali olingan JWT tokenni yuqoridagi 'Authorize' tugmasi orqali kiriting.")
                        .contact(new Contact()
                                .name("СтройОптТорг Support")
                                .email("support@stroyopttorg.local"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token kiriting. Format: Bearer <token> (yoki faqat tokenni o'zi)")));
    }
}
