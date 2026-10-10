package com.example.stroy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull(message = "Mahsulot majburiy") Long productId,
        @NotNull @Min(value = 1, message = "Miqdor kamida 1 bo'lsin") Integer quantity
) {
}
