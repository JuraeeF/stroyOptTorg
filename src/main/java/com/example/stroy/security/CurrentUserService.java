package com.example.stroy.security;

import com.example.stroy.entity.User;
import com.example.stroy.exception.ApiException;
import com.example.stroy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository users;

    public User require() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw ApiException.unauthorized("Avval tizimga kiring");
        }
        return users.findByEmail(auth.getName())
                .orElseThrow(() -> ApiException.unauthorized("Foydalanuvchi topilmadi"));
    }
}
