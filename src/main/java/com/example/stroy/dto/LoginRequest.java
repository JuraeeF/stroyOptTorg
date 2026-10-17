package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Tizimga kirish so'rovi")
public record LoginRequest(
        @Schema(description = "Foydalanuvchi email manzili", example = "admin@stroy.local")
        @NotBlank(message = "Email majburiy") String email,
        @Schema(description = "Parol", example = "Admin12345!")
        @NotBlank(message = "Parol majburiy") String password
) {
}
