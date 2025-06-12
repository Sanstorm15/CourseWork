// backend/src/main/java/com/example/journal/service/TeacherService.java
package com.example.journal.service;

import com.example.journal.model.Teacher;
import com.example.journal.model.User;
import com.example.journal.model.Role;
import com.example.journal.model.Subject;
import com.example.journal.model.Grade;
import com.example.journal.repository.TeacherRepository;
import com.example.journal.repository.UserRepository;
import com.example.journal.repository.SubjectRepository;
import com.example.journal.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private GradeRepository gradeRepository;

    /**
     * Отримання всіх викладачів
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * Отримання викладача за ID
     */
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    /**
     * Отримання викладача за User ID
     */
    public Optional<Teacher> getTeacherByUserId(Long userId) {
        return teacherRepository.findByUserId(userId);
    }

    /**
     * Створення нового викладача
     */
    public Teacher createTeacher(Teacher teacher, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Користувача не знайдено!"));

        if (user.getRole() != Role.TEACHER) {
            throw new RuntimeException("Користувач повинен мати роль TEACHER!");
        }

        if (teacherRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Профіль викладача для цього користувача вже існує!");
        }

        teacher.setUser(user);
        teacher.setCreatedAt(LocalDateTime.now());
        
        return teacherRepository.save(teacher);
    }

    /**
     * Оновлення викладача
     */
    public Teacher updateTeacher(Long id, Teacher updatedTeacher) {
        Teacher existingTeacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Викладача не знайдено!"));

        existingTeacher.setDepartment(updatedTeacher.getDepartment());
        existingTeacher.setAcademicRank(updatedTeacher.getAcademicRank());
        existingTeacher.setSpecialization(updatedTeacher.getSpecialization());
        existingTeacher.setExperience(updatedTeacher.getExperience());
        existingTeacher.setPhoneNumber(updatedTeacher.getPhoneNumber());
        existingTeacher.setOfficeNumber(updatedTeacher.getOfficeNumber());
        existingTeacher.setBio(updatedTeacher.getBio());

        return teacherRepository.save(existingTeacher);
    }

    /**
     * Видалення викладача
     */
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new RuntimeException("Викладача не знайдено!");
        }
        teacherRepository.deleteById(id);
    }

    /**
     * Пошук викладачів за відділенням
     */
    public List<Teacher> getTeachersByDepartment(String department) {
        return teacherRepository.findByDepartmentContainingIgnoreCase(department);
    }

    /**
     * Пошук викладачів за спеціалізацією
     */
    public List<Teacher> getTeachersBySpecialization(String specialization) {
        return teacherRepository.findBySpecializationContainingIgnoreCase(specialization);
    }

    /**
     * Пошук викладачів за академічним званням
     */
    public List<Teacher> getTeachersByAcademicRank(String academicRank) {
        return teacherRepository.findByAcademicRankContainingIgnoreCase(academicRank);
    }

    /**
     * Призначення предмета викладачу
     */
    public void assignSubjectToTeacher(Long teacherId, Long subjectId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Викладача не знайдено!"));

        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new RuntimeException("Предмет не знайдено!"));

        teacher.getSubjects().add(subject);
        teacherRepository.save(teacher);
    }

    /**
     * Видалення предмета у викладача
     */
    public void removeSubjectFromTeacher(Long teacherId, Long subjectId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Викладача не знайдено!"));

        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new RuntimeException("Предмет не знайдено!"));

        teacher.getSubjects().remove(subject);
        teacherRepository.save(teacher);
    }

    /**
     * Отримання предметів викладача
     */
    public List<Subject> getTeacherSubjects(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Викладача не знайдено!"));
        
        return teacher.getSubjects();
    }

    /**
     * Виставлення оцінки студенту
     */
    public Grade assignGradeToStudent(Long teacherId, Long studentId, Long subjectId, 
                                     int gradeValue, String gradeType, String comment) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Викладача не знайдено!"));

        // Перевіряємо чи викладач викладає цей предмет
        boolean teachesSubject = teacher.getSubjects().stream()
            .anyMatch(subject -> subject.getId().equals(subjectId));
        
        if (!teachesSubject) {
            throw new RuntimeException("Викладач не викладає цей предмет!");
        }

        // Створюємо нову оцінку
        Grade grade = new Grade();
        grade.setStudentId(studentId);
        grade.setSubjectId(subjectId);
        grade.setTeacherId(teacherId);
        grade.setGrade(gradeValue);
        grade.setGradeType(gradeType);
        grade.setComment(comment);
        grade.setCreatedAt(LocalDateTime.now());

        return gradeRepository.save(grade);
    }

    /**
     * Отримання всіх оцінок поставлених викладачем
     */
    public List<Grade> getGradesByTeacher(Long teacherId) {
        return gradeRepository.findByTeacherId(teacherId);
    }

    /**
     * Отримання оцінок поставлених викладачем за конкретний предмет
     */
    public List<Grade> getGradesByTeacherAndSubject(Long teacherId, Long subjectId) {
        return gradeRepository.findByTeacherIdAndSubjectId(teacherId, subjectId);
    }

    /**
     * Оновлення оцінки
     */
    public Grade updateGrade(Long gradeId, Long teacherId, int newGradeValue, String comment) {
        Grade grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new RuntimeException("Оцінку не знайдено!"));

        if (!grade.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("Тільки викладач, який поставив оцінку, може її змінити!");
        }

        grade.setGrade(newGradeValue);
        grade.setComment(comment);
        grade.setUpdatedAt(LocalDateTime.now());

        return gradeRepository.save(grade);
    }

    /**
     * Видалення оцінки
     */
    public void deleteGrade(Long gradeId, Long teacherId) {
        Grade grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new RuntimeException("Оцінку не знайдено!"));

        if (!grade.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("Тільки викладач, який поставив оцінку, може її видалити!");
        }

        gradeRepository.delete(grade);
    }

    /**
     * Отримання статистики викладача
     */
    public TeacherStats getTeacherStats(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new RuntimeException("Викладача не знайдено!"));

        int subjectsCount = teacher.getSubjects().size();
        long gradesCount = gradeRepository.countByTeacherId(teacherId);
        
        return new TeacherStats(subjectsCount, gradesCount);
    }

    /**
     * Пошук викладачів за ім'ям користувача
     */
    public List<Teacher> searchTeachersByName(String name) {
        return teacherRepository.findByUserFullNameContainingIgnoreCase(name);
    }

    /**
     * Внутрішній клас для статистики викладача
     */
    public static class TeacherStats {
        private int subjectsCount;
        private long gradesGiven;

        public TeacherStats(int subjectsCount, long gradesGiven) {
            this.subjectsCount = subjectsCount;
            this.gradesGiven = gradesGiven;
        }

        // Getters
        public int getSubjectsCount() { return subjectsCount; }
        public long getGradesGiven() { return gradesGiven; }
    }
}