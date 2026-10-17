package com.example.stroy.controller;

import com.example.stroy.dto.AuthResponse;
import com.example.stroy.dto.LoginRequest;
import com.example.stroy.dto.RegisterRequest;
import com.example.stroy.dto.UserResponse;
import com.example.stroy.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.stroy.config.OpenApiConfig.BEARER_AUTH;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "1. Autentifikatsiya", description = "Ro'yxatdan o'tish, tizimga kirish va joriy foydalanuvchi ma'lumotlari")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Yangi foydalanuvchini ro'yxatdan o'tkazish", description = "Ulgurji xaridor sifatida ro'yxatdan o'tish")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Tizimga kirish", description = "Email va parol orqali JWT token olish")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = BEARER_AUTH)
    @Operation(summary = "Joriy foydalanuvchi profili", description = "JWT token orqali kirgan foydalanuvchi profilini olish")
    public UserResponse me() {
        return authService.me();
    }
}
