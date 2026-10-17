package com.example.stroy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "API xatolik modeli")
public record ApiError(
        @Schema(description = "Xatolik yuz bergan vaqt")
        Instant timestamp,

        @Schema(description = "HTTP status kodi", example = "400")
        int status,

        @Schema(description = "HTTP status nomi", example = "Bad Request")
        String error,

        @Schema(description = "Xatolik tavsifi", example = "Validatsiya xatosi yuz berdi")
        String message,

        @Schema(description = "Maydonlar bo'yicha xatoliklar (agar validatsiya xatosi bo'lsa)")
        Map<String, String> fieldErrors
) {
}
