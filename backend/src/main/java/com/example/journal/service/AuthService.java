// backend/src/main/java/com/example/journal/service/AuthService.java
package com.example.journal.service;

import com.example.journal.dto.RegisterRequest;
import com.example.journal.model.User;
import com.example.journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User register(RegisterRequest request) {
        // Перевіряємо, чи не існує користувач з таким email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Користувач з таким email вже існує");
        }

        // Створюємо нового користувача
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(hashPassword(request.getPassword()));
        user.setRole(request.getRole());
        user.setActive(true);

        return userRepository.save(user);
    }

    public User authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Користувача з таким email не знайдено");
        }

        User user = userOpt.get();
        
        if (!user.isActive()) {
            throw new RuntimeException("Обліковий запис деактивований");
        }

        if (!verifyPassword(password, user.getPassword())) {
            throw new RuntimeException("Невірний пароль");
        }

        return user;
    }

    public String generateToken(User user) {
        // Простий токен: base64(userId:email:timestamp)
        // В продакшені варто використовувати JWT
        String tokenData = user.getId() + ":" + user.getEmail() + ":" + System.currentTimeMillis();
        return Base64.getEncoder().encodeToString(tokenData.getBytes(StandardCharsets.UTF_8));
    }

    public User validateTokenAndGetUser(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
            String[] parts = decoded.split(":");
            
            if (parts.length != 3) {
                throw new RuntimeException("Невірний формат токену");
            }

            Long userId = Long.parseLong(parts[0]);
            String email = parts[1];
            long timestamp = Long.parseLong(parts[2]);

            // Перевіряємо, чи токен не застарілий (24 години)
            long currentTime = System.currentTimeMillis();
            if (currentTime - timestamp > 24 * 60 * 60 * 1000) {
                throw new RuntimeException("Токен застарілий");
            }

            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty() || !userOpt.get().getEmail().equals(email)) {
                throw new RuntimeException("Невірний токен");
            }

            User user = userOpt.get();
            if (!user.isActive()) {
                throw new RuntimeException("Обліковий запис деактивований");
            }

            return user;
        } catch (Exception e) {
            throw new RuntimeException("Помилка валідації токену: " + e.getMessage());
        }
    }

    public void updateLastLogin(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Користувача не знайдено");
        }

        User user = userOpt.get();
        
        if (!verifyPassword(oldPassword, user.getPassword())) {
            throw new RuntimeException("Старий пароль невірний");
        }

        if (newPassword.length() < 6) {
            throw new RuntimeException("Новий пароль має бути мінімум 6 символів");
        }

        user.setPassword(hashPassword(newPassword));
        userRepository.save(user);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Помилка хешування паролю", e);
        }
    }

    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return hashPassword(plainPassword).equals(hashedPassword);
    }
}