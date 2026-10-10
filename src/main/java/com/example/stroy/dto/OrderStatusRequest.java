package com.example.stroy.dto;

import com.example.stroy.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(@NotNull(message = "Holat majburiy") OrderStatus status) {
}
