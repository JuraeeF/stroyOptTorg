package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.example.stroy.entity.Role;

@Schema(description = "Foydalanuvchi ma'lumotlari")
public record UserResponse(
        @Schema(description = "Foydalanuvchi ID", example = "1")
        Long id,
        @Schema(description = "To'liq ism", example = "Abdullayev Jasur")
        String fullName,
        @Schema(description = "Email", example = "jasur@example.com")
        String email,
        @Schema(description = "Telefon", example = "+998901234567")
        String phone,
        @Schema(description = "Kompaniya nomi", example = "Jasur Qurilish LLC")
        String companyName,
        @Schema(description = "Foydalanuvchi roli", example = "USER")
        Role role
) {
}
