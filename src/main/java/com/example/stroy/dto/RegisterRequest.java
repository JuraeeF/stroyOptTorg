package com.example.stroy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Ism majburiy") @Size(max = 150) String fullName,
        @NotBlank(message = "Email majburiy") @Email(message = "Email noto'g'ri") @Size(max = 150) String email,
        @Pattern(regexp = "^\\+?[0-9 ()-]{7,20}$", message = "Telefon raqami noto'g'ri") String phone,
        @Size(max = 200) String companyName,
        @NotBlank(message = "Parol majburiy") @Size(min = 8, max = 100, message = "Parol kamida 8 belgi bo'lsin") String password
) {
}
