// backend/src/main/java/com/example/journal/controller/AuthController.java
package com.example.journal.controller;

import com.example.journal.dto.LoginRequest;
import com.example.journal.dto.RegisterRequest;
import com.example.journal.dto.AuthResponse;
import com.example.journal.model.User;
import com.example.journal.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            String token = authService.generateToken(user);
            
            return ResponseEntity.ok(new AuthResponse(
                token,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                "Реєстрація успішна!"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse(null, null, null, null, null, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = authService.authenticate(request.getEmail(), request.getPassword());
            String token = authService.generateToken(user);
            
            authService.updateLastLogin(user.getId());
            
            return ResponseEntity.ok(new AuthResponse(
                token,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                "Вхід успішний!"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse(null, null, null, null, null, e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        // В простій реалізації просто повертаємо успішну відповідь
        // В продакшені тут би був blacklist токенів
        return ResponseEntity.ok("Вихід успішний!");
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.replace("Bearer ", "");
            User user = authService.validateTokenAndGetUser(actualToken);
            
            return ResponseEntity.ok(new AuthResponse(
                actualToken,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                "Дані користувача отримано"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new AuthResponse(null, null, null, null, null, e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody ChangePasswordRequest request) {
        try {
            String actualToken = token.replace("Bearer ", "");
            User user = authService.validateTokenAndGetUser(actualToken);
            
            authService.changePassword(user.getId(), request.getOldPassword(), request.getNewPassword());
            
            return ResponseEntity.ok("Пароль успішно змінено!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Допоміжний клас для зміни паролю
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        // Getters and Setters
        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}