package com.example.stroy.dto;

import com.example.stroy.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Buyurtma holatini yangilash so'rovi")
public record OrderStatusRequest(
        @Schema(description = "Yangi holat: NEW, CONFIRMED, SHIPPED, DELIVERED, CANCELLED", example = "CONFIRMED")
        @NotNull(message = "Holat majburiy")
        OrderStatus status
) {
}
