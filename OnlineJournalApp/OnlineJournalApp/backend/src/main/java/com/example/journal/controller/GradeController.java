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

   @RestController
public class TestController {
    
    @GetMapping("/test")
    public String testConnection() {
        return "З'єднання успішне!";
    }
    
    @GetMapping("/")  // Додав маппінг для кореневого шляху
    public String test() {
        return "Сервер працює!";
    }
}
    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    // Додаємо оцінку студенту
    @PostMapping("/{studentId}")
    public Grade addGrade(@PathVariable Long studentId, @Valid @RequestBody Grade grade) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Студента не знайдено"));
        grade.setStudent(student);
        return gradeRepository.save(grade);
    }

    // Повертаємо всі оцінки
    @GetMapping
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    // Видаляємо оцінку
    @DeleteMapping("/{id}")
    public void deleteGrade(@PathVariable Long id) {
        gradeRepository.deleteById(id);
    }
}
