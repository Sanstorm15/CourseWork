// backend/src/main/java/com/example/journal/model/Grade.java
package com.example.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Оцінка не може бути менше 1")
    @Max(value = 12, message = "Оцінка не може бути більше 12")
    private int grade;

    @Size(max = 500, message = "Коментар не може бути більше 500 символів")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private GradeType type = GradeType.REGULAR;

    // Студент, який отримав оцінку
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // Предмет, з якого поставлена оцінка
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // Викладач, який поставив оцінку
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    public enum GradeType {
        REGULAR("Звичайна"),
        EXAM("Екзамен"),
        TEST("Контрольна"),
        HOMEWORK("Домашнє завдання"),
        PROJECT("Проект"),
        PRESENTATION("Презентація");

        private final String displayName;

        GradeType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Конструктори
    public Grade() {
        this.createdAt = LocalDateTime.now();
    }

    public Grade(int grade, User student, Subject subject, User teacher) {
        this();
        this.grade = grade;
        this.student = student;
        this.subject = subject;
        this.teacher = teacher;
    }

    public Grade(int grade, String comment, User student, Subject subject, User teacher, GradeType type) {
        this();
        this.grade = grade;
        this.comment = comment;
        this.student = student;
        this.subject = subject;
        this.teacher = teacher;
        this.type = type;
    }

    // Допоміжні методи
    public String getGradeCategory() {
        if (grade >= 10) return "Відмінно";
        if (grade >= 7) return "Добре";
        if (grade >= 4) return "Задовільно";
        return "Незадовільно";
    }

    public boolean isExcellent() {
        return grade >= 10;
    }

    public boolean isPassing() {
        return grade >= 4;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public GradeType getType() {
        return type;
    }

    public void setType(GradeType type) {
        this.type = type;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}