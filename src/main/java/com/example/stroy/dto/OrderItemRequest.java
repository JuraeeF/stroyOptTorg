package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Buyurtma mahsulot elementi so'rovi")
public record OrderItemRequest(
        @Schema(description = "Mahsulot ID raqami", example = "1")
        @NotNull(message = "Mahsulot majburiy")
        Long productId,

        @Schema(description = "Buyurtma qilinayotgan miqdor", example = "20")
        @NotNull
        @Min(value = 1, message = "Miqdor kamida 1 bo'lsin")
        Integer quantity
) {
}
