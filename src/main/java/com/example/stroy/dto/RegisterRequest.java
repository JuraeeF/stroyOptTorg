package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Ro'yxatdan o'tish so'rovi")
public record RegisterRequest(
        @Schema(description = "To'liq ism", example = "Abdullayev Jasur")
        @NotBlank(message = "Ism majburiy") @Size(max = 150) String fullName,
        @Schema(description = "Email manzil", example = "jasur@example.com")
        @NotBlank(message = "Email majburiy") @Email(message = "Email noto'g'ri") @Size(max = 150) String email,
        @Schema(description = "Telefon raqami", example = "+998901234567")
        @Pattern(regexp = "^\\+?[0-9 ()-]{7,20}$", message = "Telefon raqami noto'g'ri") String phone,
        @Schema(description = "Kompaniya nomi", example = "Jasur Qurilish LLC")
        @Size(max = 200) String companyName,
        @Schema(description = "Parol (kamida 8 belgi)", example = "MyPass123!")
        @NotBlank(message = "Parol majburiy") @Size(min = 8, max = 100, message = "Parol kamida 8 belgi bo'lsin") String password
) {
}
