package com.example.journal.repository;

import com.example.journal.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    // Існуючий метод
    List<Grade> findByStudentId(Long studentId);
    
    // Пошук оцінок за викладачем
    List<Grade> findByTeacherId(Long teacherId);
    
    // Пошук оцінок за предметом
    List<Grade> findBySubjectContainingIgnoreCase(String subject);
    
    // Пошук оцінок студента за предметом
    List<Grade> findByStudentIdAndSubjectContainingIgnoreCase(Long studentId, String subject);
    
    // Пошук оцінок викладача за предметом
    List<Grade> findByTeacherIdAndSubjectContainingIgnoreCase(Long teacherId, String subject);
    
    // Пошук оцінок за датою
    List<Grade> findByCreatedDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Статистика: середня оцінка студента за предмет
    @Query("SELECT AVG(g.grade) FROM Grade g WHERE g.student.id = :studentId AND g.subject = :subject")
    Double getAverageGradeByStudentAndSubject(@Param("studentId") Long studentId, @Param("subject") String subject);
    
    // Статистика: середня оцінка студента загалом
    @Query("SELECT AVG(g.grade) FROM Grade g WHERE g.student.id = :studentId")
    Double getAverageGradeByStudent(@Param("studentId") Long studentId);
    
    // Статистика: всі оцінки викладача за період
    @Query("SELECT g FROM Grade g WHERE g.teacher.id = :teacherId AND g.createdDate BETWEEN :startDate AND :endDate")
    List<Grade> findByTeacherAndDateRange(@Param("teacherId") Long teacherId, 
                                         @Param("startDate") LocalDate startDate, 
                                         @Param("endDate") LocalDate endDate);
    
    // Пошук останніх оцінок студента
    @Query("SELECT g FROM Grade g WHERE g.student.id = :studentId ORDER BY g.createdDate DESC")
    List<Grade> findRecentGradesByStudent(@Param("studentId") Long studentId);
    
    // Топ студентів за середньою оцінкою
    @Query("SELECT g.student, AVG(g.grade) as avgGrade FROM Grade g GROUP BY g.student ORDER BY avgGrade DESC")
    List<Object[]> findTopStudentsByAverageGrade();
}