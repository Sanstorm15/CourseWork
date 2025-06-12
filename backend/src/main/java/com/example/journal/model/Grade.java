package com.example.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String subject;

    @Min(value = 1, message = "Оцінка не може бути менше 1")
    @Max(value = 12, message = "Оцінка не може бути більше 12")
    private int grade;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Grade() {}

    public Grade(String name, String subject, int grade) {
        this.name = name;
        this.subject = subject;
        this.grade = grade;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public int getGrade() {
        return grade;
    }

    public Student getStudent() {
        return student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
