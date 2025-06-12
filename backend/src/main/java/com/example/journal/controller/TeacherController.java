package com.example.journal.controller;

import com.example.journal.model.Teacher;
import com.example.journal.model.Grade;
import com.example.journal.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    // Отримати всіх викладачів (доступно всім)
    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    // Отримати викладача за ID
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        try {
            Teacher teacher = teacherService.getTeacherById(id);
            return ResponseEntity.ok(teacher);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Створити нового викладача (тільки для адмінів)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody Teacher teacher) {
        try {
            Teacher createdTeacher = teacherService.createTeacher(teacher);
            return ResponseEntity.ok(createdTeacher);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Оновити інформацію про викладача
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @teacherService.isCurrentTeacher(#id)")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @Valid @RequestBody Teacher teacher) {
        try {
            Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
            return ResponseEntity.ok(updatedTeacher);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Видалити викладача (тільки для адмінів)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTeacher(@PathVariable Long id) {
        try {
            teacherService.deleteTeacher(id);
            return ResponseEntity.ok("Викладача видалено");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Отримати всі оцінки, поставлені викладачем
    @GetMapping("/{id}/grades")
    @PreAuthorize("hasRole('ADMIN') or @teacherService.isCurrentTeacher(#id)")
    public List<Grade> getTeacherGrades(@PathVariable Long id) {
        return teacherService.getTeacherGrades(id);
    }

    // Поставити оцінку студенту (тільки для викладачів)
    @PostMapping("/{teacherId}/grades")
    @PreAuthorize("hasRole('TEACHER') and @teacherService.isCurrentTeacher(#teacherId)")
    public ResponseEntity<Grade> addGrade(@PathVariable Long teacherId, @Valid @RequestBody Grade grade) {
        try {
            Grade createdGrade = teacherService.addGrade(teacherId, grade);
            return ResponseEntity.ok(createdGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Оновити оцінку (тільки викладач, який поставив оцінку)
    @PutMapping("/{teacherId}/grades/{gradeId}")
    @PreAuthorize("hasRole('TEACHER') and @teacherService.canEditGrade(#teacherId, #gradeId)")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long teacherId, @PathVariable Long gradeId, @Valid @RequestBody Grade grade) {
        try {
            Grade updatedGrade = teacherService.updateGrade(teacherId, gradeId, grade);
            return ResponseEntity.ok(updatedGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Видалити оцінку (тільки викладач, який поставив оцінку, або адмін)
    @DeleteMapping("/{teacherId}/grades/{gradeId}")
    @PreAuthorize("hasRole('ADMIN') or (@teacherService.canEditGrade(#teacherId, #gradeId))")
    public ResponseEntity<String> deleteGrade(@PathVariable Long teacherId, @PathVariable Long gradeId) {
        try {
            teacherService.deleteGrade(teacherId, gradeId);
            return ResponseEntity.ok("Оцінку видалено");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Отримати статистику викладача
    @GetMapping("/{id}/statistics")
    @PreAuthorize("hasRole('ADMIN') or @teacherService.isCurrentTeacher(#id)")
    public ResponseEntity<Object> getTeacherStatistics(@PathVariable Long id) {
        try {
            Object statistics = teacherService.getTeacherStatistics(id);
            return ResponseEntity.ok(statistics);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Отримати список предметів викладача
    @GetMapping("/{id}/subjects")
    public List<String> getTeacherSubjects(@PathVariable Long id) {
        return teacherService.getTeacherSubjects(id);
    }
}