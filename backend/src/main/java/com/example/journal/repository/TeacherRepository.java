package com.example.journal.repository;

import com.example.journal.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    // Пошук викладача за ID користувача
    Optional<Teacher> findByUserId(Long userId);
    
    // Пошук викладачів за предметом
    List<Teacher> findBySubjectsContainingIgnoreCase(String subject);
    
    // Пошук активних викладачів
    @Query("SELECT t FROM Teacher t WHERE t.user.active = true")
    List<Teacher> findActiveTeachers();
    
    // Пошук викладачів за ім'ям (з таблиці User)
    @Query("SELECT t FROM Teacher t WHERE t.user.firstName LIKE %:name% OR t.user.lastName LIKE %:name%")
    List<Teacher> findByNameContaining(@Param("name") String name);
    
    // Пошук викладача за email користувача
    @Query("SELECT t FROM Teacher t WHERE t.user.email = :email")
    Optional<Teacher> findByUserEmail(@Param("email") String email);
    
    // Підрахунок кількості викладачів за предметом
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.subjects LIKE %:subject%")
    long countBySubject(@Param("subject") String subject);
    
    // Пошук викладачів, які викладають конкретний предмет
    @Query("SELECT t FROM Teacher t WHERE t.subjects LIKE %:subject% AND t.user.active = true")
    List<Teacher> findActiveTeachersBySubject(@Param("subject") String subject);
}