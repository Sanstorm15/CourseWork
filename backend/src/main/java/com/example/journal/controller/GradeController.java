package com.example.journal.controller;

import com.example.journal.model.Grade;
import com.example.journal.model.Student;
import com.example.journal.repository.GradeRepository;
import com.example.journal.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin
public class GradeController {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    // Повертаємо всі оцінки
    @GetMapping
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    // Додаємо оцінку (створюємо студента якщо не існує)
    @PostMapping
    public Grade addGrade(@Valid @RequestBody Grade grade) {
        // Шукаємо студента за ім'ям або створюємо нового
        Student student = studentRepository.findAll().stream()
                .filter(s -> s.getName().equals(grade.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Student newStudent = new Student(grade.getName());
                    return studentRepository.save(newStudent);
                });
        
        grade.setStudent(student);
        return gradeRepository.save(grade);
    }

    // Додаємо оцінку конкретному студенту за ID
    @PostMapping("/{studentId}")
    public Grade addGradeToStudent(@PathVariable Long studentId, @Valid @RequestBody Grade grade) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Студента не знайдено"));
        grade.setStudent(student);
        return gradeRepository.save(grade);
    }

    // Отримати оцінки конкретного студента
    @GetMapping("/student/{studentId}")
    public List<Grade> getGradesByStudent(@PathVariable Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    // Видаляємо оцінку
    @DeleteMapping("/{id}")
    public void deleteGrade(@PathVariable Long id) {
        gradeRepository.deleteById(id);
    }
}