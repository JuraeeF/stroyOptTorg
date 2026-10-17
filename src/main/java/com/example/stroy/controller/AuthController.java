package com.example.stroy.controller;

import com.example.stroy.dto.AuthResponse;
import com.example.stroy.dto.LoginRequest;
import com.example.stroy.dto.RegisterRequest;
import com.example.stroy.dto.UserResponse;
import com.example.stroy.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autentifikatsiya", description = "Ro'yxatdan o'tish, tizimga kirish va joriy foydalanuvchi ma'lumotlari")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Ro'yxatdan o'tish", description = "Yangi foydalanuvchi ro'yxatdan o'tadi va JWT token oladi")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Muvaffaqiyatli ro'yxatdan o'tildi"),
            @ApiResponse(responseCode = "400", description = "Validatsiya xatosi"),
            @ApiResponse(responseCode = "409", description = "Email allaqachon mavjud")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @Operation(summary = "Tizimga kirish", description = "Email va parol bilan kirish, JWT token olish")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Muvaffaqiyatli kirish"),
            @ApiResponse(responseCode = "401", description = "Email yoki parol noto'g'ri")
    })
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @Operation(summary = "Joriy foydalanuvchi", description = "Tizimga kirgan foydalanuvchining ma'lumotlarini qaytaradi")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Foydalanuvchi ma'lumotlari"),
            @ApiResponse(responseCode = "401", description = "Autentifikatsiya talab qilinadi")
    })
    @GetMapping("/me")
    public UserResponse me() {
        return authService.me();
    }
}
