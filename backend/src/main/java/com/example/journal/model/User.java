// backend/src/main/java/com/example/journal/model/User.java
package com.example.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ім'я обов'язкове")
    @Size(min = 2, max = 50, message = "Ім'я має бути від 2 до 50 символів")
    private String firstName;

    @NotBlank(message = "Прізвище обов'язкове")
    @Size(min = 2, max = 50, message = "Прізвище має бути від 2 до 50 символів")
    private String lastName;

    @Email(message = "Некоректний формат email")
    @NotBlank(message = "Email обов'язковий")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Пароль обов'язковий")
    @Size(min = 6, message = "Пароль має бути мінімум 6 символів")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    private boolean active = true;

    // Для студентів - їхні оцінки
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades;

    // Для викладачів - предмети які викладають
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subject> subjects;

    public enum Role {
        STUDENT("Студент"),
        TEACHER("Викладач"),
        ADMIN("Адміністратор");

        private final String displayName;

        Role(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Конструктори
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String firstName, String lastName, String email, String password, Role role) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Допоміжні методи
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isStudent() {
        return role == Role.STUDENT;
    }

    public boolean isTeacher() {
        return role == Role.TEACHER;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}