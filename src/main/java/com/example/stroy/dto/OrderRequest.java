package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Buyurtma yaratish so'rovi")
public record OrderRequest(
        @Schema(description = "Mijozning to'liq ismi", example = "Иван Петров")
        @NotBlank(message = "Ism majburiy")
        @Size(max = 150)
        String customerName,

        @Schema(description = "Telefon raqami", example = "+998 90 123 45 67")
        @NotBlank(message = "Telefon majburiy")
        @Size(max = 30)
        String phone,

        @Schema(description = "Yetkazib berish manzili", example = "г. Ташкент, ул. Строителей, д. 15")
        @NotBlank(message = "Manzil majburiy")
        @Size(max = 500)
        String address,

        @Schema(description = "Buyurtmaga izoh", example = "Разгрузка манипулятором, доставка в первой половине дня")
        @Size(max = 1000)
        String comment,

        @Schema(description = "Buyurtmadagi mahsulotlar ro'yxati")
        @NotEmpty(message = "Buyurtma bo'sh bo'lishi mumkin emas")
        List<@Valid OrderItemRequest> items
) {
}
