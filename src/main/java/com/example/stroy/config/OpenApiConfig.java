package com.example.stroy.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "BearerAuth";

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("СтройОптТорг — Backend REST API")
                        .version("1.0.0")
                        .description("""
                                ### СтройОптТорг — Ulgurji qurilish materiallari savdo platformasi API hujjatlari
                                
                                Ushbu tizim orqali mijozlar qurilish materiallarini ulgurji narxlarda ko'rishi, qidirishi,
                                savatchaga qo'shishi va buyurtma rasmiylashtirishi mumkin.
                                
                                #### Autentifikatsiya:
                                1. `/api/auth/login` yoki `/api/auth/register` orqali tizimga kiring va JWT token oling.
                                2. Yuqoridagi **Authorize** tugmasini bosing va olingan tokenni kiriting.
                                """)
                        .contact(new Contact()
                                .name("СтройОптТорг")
                                .email("support@stroyopttorg.local"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Mahalliy dasturlash serveri")
                ))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token kiriting. Format: Bearer <token> (yoki faqat token)")))
                .tags(List.of(
                        new Tag().name("1. Autentifikatsiya").description("Ro'yxatdan o'tish, kirish va profil ma'lumotlari"),
                        new Tag().name("2. Kategoriyalar").description("Mahsulot toifalari katalogi"),
                        new Tag().name("3. Mahsulotlar").description("Mahsulotlar katalogi, qidirish, filtr va saralash"),
                        new Tag().name("4. Buyurtmalar").description("Mijoz buyurtmalarini yaratish, ko'rish va bekor qilish"),
                        new Tag().name("5. Aloqa / Bog'lanish").description("Sayt orqali mijoz murojaatlari va xabarlar"),
                        new Tag().name("6. Admin — Katalog boshqaruvi").description("Kategoriya va mahsulotlarni boshqarish (Faqat ADMIN)"),
                        new Tag().name("7. Admin — Buyurtmalar boshqaruvi").description("Barcha buyurtmalar, holat o'zgartirish va statistika (Faqat ADMIN)"),
                        new Tag().name("8. Admin — Aloqa xabarlari").description("Mijoz murojaatlarini ko'rish va qayta ishlash (Faqat ADMIN)")
                ));
    }
}
