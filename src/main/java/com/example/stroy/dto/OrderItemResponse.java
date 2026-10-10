package com.example.stroy.dto;

import java.math.BigDecimal;

public record OrderItemResponse(Long productId, String productName, String unit, BigDecimal unitPrice,
                                int quantity, BigDecimal lineTotal) {
}
