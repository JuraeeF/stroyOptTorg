package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kategoriya ma'lumotlari javobi")
public record CategoryResponse(
        @Schema(description = "Kategoriya ID raqami", example = "1")
        Long id,
        @Schema(description = "Kategoriya nomi", example = "Цемент и сухие смеси")
        String name,
        @Schema(description = "URL slug", example = "cement-i-suhie-smesi")
        String slug,
        @Schema(description = "Kategoriya tavsifi", example = "Цемент, штукатурки, клеи и шпаклёвки")
        String description,
        @Schema(description = "Rasm URL", example = "https://example.com/cement.jpg")
        String imageUrl
) {
}
