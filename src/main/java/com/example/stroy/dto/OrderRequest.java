package com.example.stroy.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(
        @NotBlank(message = "Ism majburiy") @Size(max = 150) String customerName,
        @NotBlank(message = "Telefon majburiy") @Size(max = 30) String phone,
        @NotBlank(message = "Manzil majburiy") @Size(max = 500) String address,
        @Size(max = 1000) String comment,
        @NotEmpty(message = "Buyurtma bo'sh bo'lishi mumkin emas") List<@Valid OrderItemRequest> items
) {
}
