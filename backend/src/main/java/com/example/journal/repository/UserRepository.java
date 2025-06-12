package com.example.journal.repository;

import com.example.journal.model.User;
import com.example.journal.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Пошук користувача за email (для авторизації)
    Optional<User> findByEmail(String email);
    
    // Пошук користувача за username
    Optional<User> findByUsername(String username);
    
    // Перевірка чи існує користувач з таким email
    boolean existsByEmail(String email);
    
    // Перевірка чи існує користувач з таким username
    boolean existsByUsername(String username);
    
    // Пошук користувачів за роллю
    List<User> findByRole(Role role);
    
    // Пошук активних користувачів
    List<User> findByActiveTrue();
    
    // Пошук користувачів за роллю і статусом активності
    List<User> findByRoleAndActive(Role role, boolean active);
}