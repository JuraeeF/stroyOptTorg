package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Mahsulot yaratish/tahrirlash so'rovi")
public record ProductRequest(
        @Schema(description = "Mahsulot nomi", example = "Sement M400 Qizilqum")
        @NotBlank(message = "Nomi majburiy") @Size(max = 250) String name,
        @Schema(description = "Artikul (SKU)", example = "CEM-M400-50")
        @NotBlank(message = "Artikul (SKU) majburiy") @Size(max = 60) String sku,
        @Schema(description = "Mahsulot tavsifi", example = "Yuqori sifatli portlandsement, 50 kg qop")
        String description,
        @Schema(description = "Narx (so'm)", example = "65000.00")
        @NotNull(message = "Narx majburiy") @DecimalMin(value = "0.01", message = "Narx 0 dan katta bo'lsin") BigDecimal price,
        @Schema(description = "O'lchov birligi", example = "qop")
        @NotBlank(message = "O'lchov birligi majburiy") @Size(max = 30) String unit,
        @Schema(description = "Minimal buyurtma miqdori", example = "10")
        @NotNull @Min(value = 1, message = "Minimal buyurtma kamida 1") Integer minOrderQuantity,
        @Schema(description = "Ombordagi qoldiq", example = "500")
        @NotNull @Min(value = 0, message = "Qoldiq manfiy bo'lmasin") Integer stock,
        @Schema(description = "Rasm URL manzili", example = "https://example.com/images/cement.jpg")
        @Size(max = 500) String imageUrl,
        @Schema(description = "Kategoriya ID raqami", example = "1")
        @NotNull(message = "Kategoriya majburiy") Long categoryId,
        @Schema(description = "Faol holati", example = "true")
        Boolean active
) {
}
