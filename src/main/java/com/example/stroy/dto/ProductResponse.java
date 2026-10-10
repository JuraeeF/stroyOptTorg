package com.example.stroy.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id, String name, String sku, String description, BigDecimal price, String unit,
        int minOrderQuantity, int stock, String imageUrl, boolean active,
        Long categoryId, String categoryName
) {
}
