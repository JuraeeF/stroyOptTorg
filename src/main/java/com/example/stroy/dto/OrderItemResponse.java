package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Buyurtma ichidagi mahsulot tafsilotlari")
public record OrderItemResponse(
        @Schema(description = "Mahsulot ID raqami", example = "1")
        Long productId,

        @Schema(description = "Mahsulot nomi (arxivlangan)", example = "Цемент М400 (50 кг)")
        String productName,

        @Schema(description = "O'lchov birligi", example = "мешок")
        String unit,

        @Schema(description = "Birlik narxi (so'm)", example = "62000.00")
        BigDecimal unitPrice,

        @Schema(description = "Miqdor", example = "20")
        int quantity,

        @Schema(description = "Ushbu qatorning umumiy summasi (so'm)", example = "1240000.00")
        BigDecimal lineTotal
) {
}
