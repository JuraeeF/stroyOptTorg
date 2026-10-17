package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Aloqa xabari javobi")
public record ContactResponse(
        @Schema(description = "Xabar ID", example = "1")
        Long id,

        @Schema(description = "Yuboruvchi ismi", example = "Алексей")
        String name,

        @Schema(description = "Telefon", example = "+998 90 987 65 43")
        String phone,

        @Schema(description = "Xabar matni")
        String message,

        @Schema(description = "Qayta ishlanganlik holati", example = "false")
        boolean processed,

        @Schema(description = "Yuborilgan vaqti")
        Instant createdAt
) {
}
