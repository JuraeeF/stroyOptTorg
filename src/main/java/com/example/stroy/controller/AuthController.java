package com.example.stroy.controller;

import com.example.stroy.dto.AuthResponse;
import com.example.stroy.dto.LoginRequest;
import com.example.stroy.dto.RegisterRequest;
import com.example.stroy.dto.UserResponse;
import com.example.stroy.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "1. Autentifikatsiya", description = "Ro'yxatdan o'tish, tizimga kirish va joriy foydalanuvchi profili")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Yangi foydalanuvchini ro'yxatdan o'tkazish", description = "Mijoz ma'lumotlarini qabul qilib, yangi hisob yaratadi va JWT token qaytaradi")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Muvaffaqiyatli ro'yxatdan o'tildi"),
            @ApiResponse(responseCode = "400", description = "Kiritilgan ma'lumotlar noto'g'ri (validatsiya xatosi)"),
            @ApiResponse(responseCode = "409", description = "Bunday email manzilli foydalanuvchi allaqachon mavjud")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @Operation(summary = "Tizimga kirish", description = "Email va parol orqali tizimga kirish va JWT access token olish")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli kirildi"),
            @ApiResponse(responseCode = "401", description = "Email yoki parol noto'g'ri")
    })
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @Operation(summary = "Joriy foydalanuvchi ma'lumotlari", description = "Yuborilgan JWT token orqali tizimga kirgan foydalanuvchi ma'lumotlarini qaytaradi")
    @SecurityRequirement(name = BEARER_AUTH)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Foydalanuvchi profili"),
            @ApiResponse(responseCode = "401", description = "Avtorizatsiyadan o'tilmagan")
    })
    @GetMapping("/me")
    public UserResponse me() {
        return authService.me();
    }
}
