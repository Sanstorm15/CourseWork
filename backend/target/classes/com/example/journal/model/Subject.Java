package com.example.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Назва предмету обов'язкова")
    @Size(min = 2, max = 100, message = "Назва предмету має бути від 2 до 100 символів")
    @Column(unique = true)
    private String name;

    @Size(max = 500, message = "Опис не може бути більше 500 символів")
    private String description;

    // Викладач, який веде цей предмет
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    // Оцінки з цього предмету
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Grade> grades;

    private boolean active = true;

    // Конструктори
    public Subject() {}

    public Subject(String name, String description, User teacher) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
    }

    public Subject(String name, User teacher) {
        this.name = name;
        this.teacher = teacher;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}