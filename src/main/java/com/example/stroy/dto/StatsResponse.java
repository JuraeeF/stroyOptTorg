package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Admin boshqaruv paneli statistikasi")
public record StatsResponse(
        @Schema(description = "Jami buyurtmalar soni", example = "45")
        long totalOrders,

        @Schema(description = "Yangi buyurtmalar soni (ko'rib chiqilishi kerak bo'lgan)", example = "7")
        long newOrders,

        @Schema(description = "Jami tushum (so'm)", example = "350000000.00")
        BigDecimal revenue,

        @Schema(description = "Faol mahsulotlar soni", example = "128")
        long activeProducts,

        @Schema(description = "Mijozlar soni", example = "84")
        long customers
) {
}
