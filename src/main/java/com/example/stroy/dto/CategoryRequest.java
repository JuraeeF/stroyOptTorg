package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Kategoriya yaratish/tahrirlash so'rovi")
public record CategoryRequest(
        @Schema(description = "Kategoriya nomi", example = "Sement va qorishma")
        @NotBlank(message = "Nomi majburiy") @Size(max = 150) String name,
        @Schema(description = "URL slug (avtomatik generatsiya qilinadi agar bo'sh bo'lsa)", example = "sement-va-qorishma")
        @Size(max = 160) String slug,
        @Schema(description = "Tavsif", example = "Sement, quruq qorishmalar va gips mahsulotlari")
        @Size(max = 1000) String description,
        @Schema(description = "Rasm URL", example = "https://example.com/images/cement-category.jpg")
        @Size(max = 500) String imageUrl
) {
}
