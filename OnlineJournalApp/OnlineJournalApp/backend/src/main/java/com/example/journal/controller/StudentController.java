package com.example.journal.controller;

import com.example.journal.model.Student;
import com.example.journal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Отримати всіх студентів
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Додати нового студента
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }
}
