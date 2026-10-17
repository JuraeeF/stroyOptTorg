package com.example.stroy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Sahifalangan ma'lumotlar strukturasining umumiy modeli")
public record PageResponse<T>(
        @Schema(description = "Joriy sahifadagi elementlar ro'yxati")
        List<T> content,

        @Schema(description = "Sahifa raqami (0 dan boshlanadi)", example = "0")
        int page,

        @Schema(description = "Sahifadagi elementlar limiti", example = "12")
        int size,

        @Schema(description = "Barcha sahifalardagi umumiy elementlar soni", example = "48")
        long totalElements,

        @Schema(description = "Jami sahifalar soni", example = "4")
        int totalPages
) {

    public static <T> PageResponse<T> from(Page<T> p) {
        return new PageResponse<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
    }
}
