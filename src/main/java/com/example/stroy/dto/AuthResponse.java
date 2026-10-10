package com.example.stroy.dto;

public record AuthResponse(String token, String tokenType, UserResponse user) {
}
