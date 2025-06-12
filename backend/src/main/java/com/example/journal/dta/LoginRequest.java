// backend/src/main/java/com/example/journal/dto/LoginRequest.java
package com.example.journal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @Email(message = "Некоректний формат email")
    @NotBlank(message = "Email обов'язковий")
    private String email;

    @NotBlank(message = "Пароль обов'язковий")
    private String password;

    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

// backend/src/main/java/com/example/journal/dto/RegisterRequest.java
package com.example.journal.dto;

import com.example.journal.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "Ім'я обов'язкове")
    @Size(min = 2, max = 50, message = "Ім'я має бути від 2 до 50 символів")
    private String firstName;

    @NotBlank(message = "Прізвище обов'язкове")
    @Size(min = 2, max = 50, message = "Прізвище має бути від 2 до 50 символів")
    private String lastName;

    @Email(message = "Некоректний формат email")
    @NotBlank(message = "Email обов'язковий")
    private String email;

    @NotBlank(message = "Пароль обов'язковий")
    @Size(min = 6, message = "Пароль має бути мінімум 6 символів")
    private String password;

    @NotNull(message = "Роль обов'язкова")
    private User.Role role;

    public RegisterRequest() {}

    public RegisterRequest(String firstName, String lastName, String email, String password, User.Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}

// backend/src/main/java/com/example/journal/dto/AuthResponse.java
package com.example.journal.dto;

public class AuthResponse {
    private String token;
    private Long userId;
    private String fullName;
    private String email;
    private String role;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String token, Long userId, String fullName, String email, String role, String message) {
        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}