package com.example.stroy.dto;

import com.example.stroy.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Schema(description = "Buyurtma ma'lumotlari javobi")
public record OrderResponse(
        @Schema(description = "Buyurtma ID raqami", example = "101")
        Long id,

        @Schema(description = "Buyurtma holati", example = "NEW")
        OrderStatus status,

        @Schema(description = "Mijoz ismi", example = "Иван Петров")
        String customerName,

        @Schema(description = "Mijoz telefoni", example = "+998 90 123 45 67")
        String phone,

        @Schema(description = "Yetkazib berish manzili", example = "г. Ташкент, ул. Строителей, д. 15")
        String address,

        @Schema(description = "Izoh", example = "Разгрузка манипулятором")
        String comment,

        @Schema(description = "Buyurtmaning umumiy summasi", example = "1240000.00")
        BigDecimal totalAmount,

        @Schema(description = "Yaratilgan vaqti")
        Instant createdAt,

        @Schema(description = "Buyurtmachi foydalanuvchi ID raqami", example = "2")
        Long userId,

        @Schema(description = "Buyurtmachi foydalanuvchi emaili", example = "ivan@example.com")
        String userEmail,

        @Schema(description = "Buyurtmadagi mahsulotlar ro'yxati")
        List<OrderItemResponse> items
) {
}
