// backend/src/main/java/com/example/journal/service/UserService.java
package com.example.journal.service;

import com.example.journal.model.User;
import com.example.journal.model.Role;
import com.example.journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Отримання всіх користувачів
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Отримання користувачів з пагінацією
     */
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Отримання користувача за ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Отримання користувача за email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Отримання всіх студентів
     */
    public List<User> getAllStudents() {
        return userRepository.findByRole(Role.STUDENT);
    }

    /**
     * Отримання всіх викладачів
     */
    public List<User> getAllTeachers() {
        return userRepository.findByRole(Role.TEACHER);
    }

    /**
     * Отримання активних користувачів
     */
    public List<User> getActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    /**
     * Отримання неактивних користувачів
     */
    public List<User> getInactiveUsers() {
        return userRepository.findByActiveFalse();
    }

    /**
     * Пошук користувачів за ім'ям
     */
    public List<User> searchUsersByName(String name) {
        return userRepository.findByFullNameContainingIgnoreCase(name);
    }

    /**
     * Пошук користувачів за email
     */
    public List<User> searchUsersByEmail(String email) {
        return userRepository.findByEmailContainingIgnoreCase(email);
    }

    /**
     * Створення нового користувача
     */
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Користувач з таким email вже існує!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);

        return userRepository.save(user);
    }

    /**
     * Оновлення користувача
     */
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Користувача не знайдено!"));

        // Перевіряємо email на унікальність (якщо він змінюється)
        if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
            userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
            throw new RuntimeException("Користувач з таким email вже існує!");
        }

        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());
        
        // Пароль оновлюємо тільки якщо він переданий
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        existingUser.setRole(updatedUser.getRole());

        return userRepository.save(existingUser);
    }

    /**
     * Видалення користувача
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Користувача не знайдено!");
        }
        userRepository.deleteById(id);
    }

    /**
     * Деактивація користувача
     */
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Користувача не знайдено!"));
        
        user.setActive(false);
        userRepository.save(user);
    }

    /**
     * Активація користувача
     */
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Користувача не знайдено!"));
        
        user.setActive(true);
        userRepository.save(user);
    }

    /**
     * Зміна ролі користувача
     */
    public void changeUserRole(Long id, Role newRole) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Користувача не знайдено!"));
        
        user.setRole(newRole);
        userRepository.save(user);
    }

    /**
     * Скидання паролю користувача (генерує новий тимчасовий пароль)
     */
    public String resetPassword(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Користувача не знайдено!"));
        
        // Генеруємо тимчасовий пароль
        String tempPassword = generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);
        
        return tempPassword;
    }

    /**
     * Перевірка існування користувача за email
     */
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Підрахунок кількості користувачів за роллю
     */
    public long countUsersByRole(Role role) {
        return userRepository.countByRole(role);
    }

    /**
     * Підрахунок активних користувачів
     */
    public long countActiveUsers() {
        return userRepository.countByActiveTrue();
    }

    /**
     * Отримання останніх зареєстрованих користувачів
     */
    public List<User> getRecentUsers(int limit) {
        return userRepository.findTop10ByOrderByCreatedAtDesc();
    }

    /**
     * Генерація тимчасового паролю
     */
    private String generateTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder tempPassword = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            tempPassword.append(chars.charAt(index));
        }
        
        return tempPassword.toString();
    }

    /**
     * Отримання статистики користувачів
     */
    public UserStats getUserStats() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByActiveTrue();
        long students = userRepository.countByRole(Role.STUDENT);
        long teachers = userRepository.countByRole(Role.TEACHER);
        
        return new UserStats(totalUsers, activeUsers, students, teachers);
    }

    /**
     * Внутрішній клас для статистики
     */
    public static class UserStats {
        private long totalUsers;
        private long activeUsers;
        private long students;
        private long teachers;

        public UserStats(long totalUsers, long activeUsers, long students, long teachers) {
            this.totalUsers = totalUsers;
            this.activeUsers = activeUsers;
            this.students = students;
            this.teachers = teachers;
        }

        // Getters
        public long getTotalUsers() { return totalUsers; }
        public long getActiveUsers() { return activeUsers; }
        public long getStudents() { return students; }
        public long getTeachers() { return teachers; }
    }
}