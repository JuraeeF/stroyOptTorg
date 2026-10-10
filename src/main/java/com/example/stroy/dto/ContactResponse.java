package com.example.stroy.dto;

import java.time.Instant;

public record ContactResponse(Long id, String name, String phone, String message, boolean processed, Instant createdAt) {
}
