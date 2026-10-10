package com.example.stroy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "Nomi majburiy") @Size(max = 150) String name,
        @Size(max = 160) String slug,
        @Size(max = 1000) String description,
        @Size(max = 500) String imageUrl
) {
}
