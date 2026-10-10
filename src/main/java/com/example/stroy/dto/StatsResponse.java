package com.example.stroy.dto;

import java.math.BigDecimal;

public record StatsResponse(long totalOrders, long newOrders, BigDecimal revenue, long activeProducts, long customers) {
}
