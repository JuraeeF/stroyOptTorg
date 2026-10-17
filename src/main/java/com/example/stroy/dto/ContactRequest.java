package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Aloqa/bog'lanish formasi so'rovi")
public record ContactRequest(
        @Schema(description = "Ism", example = "Алексей")
        @NotBlank(message = "Ism majburiy")
        @Size(max = 150)
        String name,

        @Schema(description = "Telefon raqami", example = "+998 90 987 65 43")
        @NotBlank(message = "Telefon majburiy")
        @Size(max = 30)
        String phone,

        @Schema(description = "Murojaat xabari", example = "Здравствуйте! Интересует оптовая закупка арматуры от 10 тонн.")
        @Size(max = 2000)
        String message
) {
}
