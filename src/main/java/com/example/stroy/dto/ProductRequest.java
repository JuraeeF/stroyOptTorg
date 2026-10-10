package com.example.stroy.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Nomi majburiy") @Size(max = 250) String name,
        @NotBlank(message = "Artikul (SKU) majburiy") @Size(max = 60) String sku,
        String description,
        @NotNull(message = "Narx majburiy") @DecimalMin(value = "0.01", message = "Narx 0 dan katta bo'lsin") BigDecimal price,
        @NotBlank(message = "O'lchov birligi majburiy") @Size(max = 30) String unit,
        @NotNull @Min(value = 1, message = "Minimal buyurtma kamida 1") Integer minOrderQuantity,
        @NotNull @Min(value = 0, message = "Qoldiq manfiy bo'lmasin") Integer stock,
        @Size(max = 500) String imageUrl,
        @NotNull(message = "Kategoriya majburiy") Long categoryId,
        Boolean active
) {
}
