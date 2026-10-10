package com.example.stroy.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email majburiy") String email,
        @NotBlank(message = "Parol majburiy") String password
) {
}
