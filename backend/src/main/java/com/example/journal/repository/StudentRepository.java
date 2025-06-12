package com.example.journal.repository;

import com.example.journal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Пошук студента за ім'ям (застаріло, залишено для сумісності)
    Optional<Student> findByName(String name);
    
    // Пошук студента за ID користувача
    Optional<Student> findByUserId(Long userId);
    
    // Пошук активних студентів
    @Query("SELECT s FROM Student s WHERE s.user.active = true")
    List<Student> findActiveStudents();
    
    // Пошук студентів за ім'ям (з таблиці User)
    @Query("SELECT s FROM Student s WHERE s.user.firstName LIKE %:name% OR s.user.lastName LIKE %:name%")
    List<Student> findByNameContaining(@Param("name") String name);
    
    // Пошук студента за email користувача
    @Query("SELECT s FROM Student s WHERE s.user.email = :email")
    Optional<Student> findByUserEmail(@Param("email") String email);
    
    // Пошук студентів за класом
    List<Student> findByClassNameContainingIgnoreCase(String className);
    
    // Пошук студентів активних в конкретному класі
    @Query("SELECT s FROM Student s WHERE s.className LIKE %:className% AND s.user.active = true")
    List<Student> findActiveStudentsByClass(@Param("className") String className);
    
    // Підрахунок студентів в класі
    long countByClassName(String className);
}