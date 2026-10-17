package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Mahsulot ma'lumotlari javobi")
public record ProductResponse(
        @Schema(description = "Mahsulot ID", example = "1") Long id,
        @Schema(description = "Nomi", example = "Sement M400 Qizilqum") String name,
        @Schema(description = "Artikul", example = "CEM-M400-50") String sku,
        @Schema(description = "Tavsif") String description,
        @Schema(description = "Narx", example = "65000.00") BigDecimal price,
        @Schema(description = "O'lchov birligi", example = "qop") String unit,
        @Schema(description = "Minimal buyurtma", example = "10") int minOrderQuantity,
        @Schema(description = "Qoldiq", example = "500") int stock,
        @Schema(description = "Rasm URL") String imageUrl,
        @Schema(description = "Faol holati", example = "true") boolean active,
        @Schema(description = "Kategoriya ID", example = "1") Long categoryId,
        @Schema(description = "Kategoriya nomi", example = "Sement va qorishma") String categoryName
) {
}
