package com.example.journal.controller;

import com.example.journal.model.User;
import com.example.journal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    // Отримати всіх користувачів (тільки для адмінів)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Отримати поточного користувача
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Отримати користувача за ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isCurrentUser(#id)")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Оновити профіль користувача
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.isCurrentUser(#id)")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Змінити пароль
    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or @userService.isCurrentUser(#id)")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody Map<String, String> passwords) {
        try {
            String oldPassword = passwords.get("oldPassword");
            String newPassword = passwords.get("newPassword");
            
            userService.changePassword(id, oldPassword, newPassword);
            return ResponseEntity.ok("Пароль успішно змінено");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Видалити користувача (тільки для адмінів)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Користувача видалено");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Змінити роль користувача (тільки для адмінів)
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> changeUserRole(@PathVariable Long id, @RequestBody Map<String, String> roleData) {
        try {
            String newRole = roleData.get("role");
            User updatedUser = userService.changeUserRole(id, newRole);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Активувати/деактивувати користувача (тільки для адмінів)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> changeUserStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> statusData) {
        try {
            Boolean isActive = statusData.get("isActive");
            User updatedUser = userService.changeUserStatus(id, isActive);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Отримати користувачів за роллю (тільки для адмінів)
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsersByRole(@PathVariable String role) {
        return userService.getUsersByRole(role);
    }

    // Пошук користувачів (тільки для адмінів)
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> searchUsers(@RequestParam String query) {
        return userService.searchUsers(query);
    }

    // Статистика користувачів (тільки для адмінів)
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getUserStatistics() {
        try {
            Object statistics = userService.getUserStatistics();
            return ResponseEntity.ok(statistics);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}