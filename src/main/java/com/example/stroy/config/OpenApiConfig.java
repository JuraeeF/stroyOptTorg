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

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    public OpenAPI stroyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("СтройОптТорг API")
                        .description("""
                                **СтройОптТорг** — ulgurji qurilish materiallari onlayn do'koni uchun backend REST API.
                                
                                ### Asosiy imkoniyatlar:
                                - 🔐 **Autentifikatsiya** — Ro'yxatdan o'tish, tizimga kirish (JWT)
                                - 📦 **Mahsulotlar** — Katalogni ko'rish, qidirish, filtrlash
                                - 🗂️ **Kategoriyalar** — Toifalarni boshqarish
                                - 🛒 **Buyurtmalar** — Buyurtma yaratish va kuzatish
                                - 📞 **Aloqa** — Mijozlarning murojaat xabarlari
                                - 🛡️ **Admin panel** — Katalog, buyurtma va xabarlarni boshqarish
                                
                                ### Autentifikatsiya:
                                1. `/api/auth/register` yoki `/api/auth/login` orqali token oling
                                2. "Authorize" tugmasini bosing va `Bearer <token>` kiriting
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("СтройОптТорг")
                                .email("admin@stroy.local"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local development server")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Bearer Authentication")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token kiriting. Login/register endpointdan olasiz.")))
                .tags(List.of(
                        new Tag().name("Autentifikatsiya").description("Ro'yxatdan o'tish, kirish va foydalanuvchi ma'lumotlari"),
                        new Tag().name("Kategoriyalar").description("Mahsulot toifalarini ko'rish"),
                        new Tag().name("Mahsulotlar").description("Mahsulotlar katalogi, qidirish va filtrlash"),
                        new Tag().name("Buyurtmalar").description("Buyurtma yaratish va kuzatish"),
                        new Tag().name("Aloqa").description("Bog'lanish xabarlari"),
                        new Tag().name("Admin — Katalog").description("Kategoriya va mahsulotlarni boshqarish (ADMIN)"),
                        new Tag().name("Admin — Buyurtmalar").description("Buyurtmalarni boshqarish va statistika (ADMIN)"),
                        new Tag().name("Admin — Xabarlar").description("Aloqa xabarlarini ko'rish va boshqarish (ADMIN)")
                ));
    }
}
