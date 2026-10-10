package com.example.stroy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactRequest(
        @NotBlank(message = "Ism majburiy") @Size(max = 150) String name,
        @NotBlank(message = "Telefon majburiy") @Size(max = 30) String phone,
        @Size(max = 2000) String message
) {
}
