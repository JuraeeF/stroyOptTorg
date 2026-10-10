package com.example.stroy.dto;

import com.example.stroy.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id, OrderStatus status, String customerName, String phone, String address, String comment,
        BigDecimal totalAmount, Instant createdAt, Long userId, String userEmail, List<OrderItemResponse> items
) {
}
