package com.example.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Зв'язок з користувачем (якщо студент має акаунт)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotBlank(message = "Ім'я студента обов'язкове")
    private String name;

    private String studentNumber; // Номер студентського квитка

    private String parentPhone; // Телефон батьків

    private String parentEmail; // Email батьків

    // Група студента
    @ManyToOne
    @JoinColumn(name = "group_id")
    private StudentGroup group;

    // Оцінки студента
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades;

    // Конструктори
    public Student() {}

    public Student(String name) {
        this.name = name;
    }

    public Student(User user, String name, String studentNumber) {
        this.user = user;
        this.name = name;
        this.studentNumber = studentNumber;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public StudentGroup getGroup() {
        return group;
    }

    public void setGroup(StudentGroup group) {
        this.group = group;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    // Допоміжні методи
    public String getFullName() {
        return user != null ? user.getFullName() : name;
    }

    public String getGroupName() {
        return group != null ? group.getName() : "Без групи";
    }

    public boolean hasUser() {
        return user != null;
    }

    // Обчислення середнього балу
    public double getAverageGrade() {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        return grades.stream()
                .mapToInt(Grade::getGrade)
                .average()
                .orElse(0.0);
    }

    // Обчислення середнього балу по предмету
    public double getAverageGradeBySubject(String subject) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        return grades.stream()
                .filter(grade -> subject.equals(grade.getSubject()))
                .mapToInt(Grade::getGrade)
                .average()
                .orElse(0.0);
    }
}