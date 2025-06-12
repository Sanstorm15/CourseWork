package com.example.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Зв'язок з користувачем
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotBlank(message = "Кафедра обов'язкова")
    private String department;

    private String phoneNumber;

    private String officeRoom;

    // Предмети, які викладає цей викладач
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "teacher_subjects", 
                     joinColumns = @JoinColumn(name = "teacher_id"))
    @Column(name = "subject")
    private Set<String> subjects;

    // Оцінки, поставлені цим викладачем
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Grade> grades;

    // Групи, які викладає цей викладач
    @ManyToMany(mappedBy = "teachers")
    private Set<StudentGroup> groups;

    // Конструктори
    public Teacher() {}

    public Teacher(User user, String department) {
        this.user = user;
        this.department = department;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOfficeRoom() {
        return officeRoom;
    }

    public void setOfficeRoom(String officeRoom) {
        this.officeRoom = officeRoom;
    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<String> subjects) {
        this.subjects = subjects;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public Set<StudentGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<StudentGroup> groups) {
        this.groups = groups;
    }

    // Допоміжні методи
    public String getFullName() {
        return user != null ? user.getFullName() : "Невідомий викладач";
    }

    public boolean teachesSubject(String subject) {
        return subjects != null && subjects.contains(subject);
    }

    public void addSubject(String subject) {
        if (subjects != null) {
            subjects.add(subject);
        }
    }

    public void removeSubject(String subject) {
        if (subjects != null) {
            subjects.remove(subject);
        }
    }
}