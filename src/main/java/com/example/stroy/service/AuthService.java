package com.example.stroy.service;

import com.example.stroy.dto.AuthResponse;
import com.example.stroy.dto.LoginRequest;
import com.example.stroy.dto.RegisterRequest;
import com.example.stroy.dto.UserResponse;
import com.example.stroy.entity.Role;
import com.example.stroy.entity.User;
import com.example.stroy.exception.ApiException;
import com.example.stroy.repository.UserRepository;
import com.example.stroy.security.CurrentUserService;
import com.example.stroy.security.JwtService;
import com.example.stroy.util.Mappers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CurrentUserService currentUser;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        String email = normalize(req.email());
        if (users.existsByEmail(email)) {
            throw ApiException.conflict("Bu email allaqachon ro'yxatdan o'tgan");
        }
        User user = users.save(User.builder()
                .fullName(req.fullName().trim())
                .email(email)
                .phone(req.phone())
                .companyName(req.companyName())
                .password(encoder.encode(req.password()))
                .role(Role.CUSTOMER)
                .build());
        return toAuth(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        String email = normalize(req.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, req.password()));
        User user = users.findByEmail(email)
                .orElseThrow(() -> ApiException.unauthorized("Email yoki parol noto'g'ri"));
        return toAuth(user);
    }

    @Transactional(readOnly = true)
    public UserResponse me() {
        return Mappers.toUser(currentUser.require());
    }

    private AuthResponse toAuth(User user) {
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, "Bearer", Mappers.toUser(user));
    }

    private static String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
