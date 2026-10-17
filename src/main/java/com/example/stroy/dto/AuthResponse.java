package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Autentifikatsiya javobi — JWT token va foydalanuvchi ma'lumotlari")
public record AuthResponse(
        @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,
        @Schema(description = "Token turi", example = "Bearer")
        String tokenType,
        @Schema(description = "Foydalanuvchi ma'lumotlari")
        UserResponse user
) {
}
