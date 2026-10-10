package com.example.stroy.dto;

import com.example.stroy.entity.Role;

public record UserResponse(Long id, String fullName, String email, String phone, String companyName, Role role) {
}
