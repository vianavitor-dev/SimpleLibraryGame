package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StudentRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentStatsRepository statsRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student("student1", "encoded_pass");
        testStudent.setName("John Student");
        entityManager.persist(testStudent);
        entityManager.flush();
    }

    @Test
    void shouldSaveStudent() {
        Student newStudent = new Student("new_student", "new_pass");
        newStudent.setName("Jane Student");

        Student saved = studentRepository.save(newStudent);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("new_student");
        assertThat(saved.getName()).isEqualTo("Jane Student");
        assertThat(saved.isActive()).isTrue();
    }

    @Test
    void shouldFindById() {
        Optional<Student> found = studentRepository.findById(testStudent.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("student1");
    }

    @Test
    void shouldFindAll() {
        Student student2 = new Student("student2", "pass2");
        studentRepository.save(student2);

        Iterable<Student> students = studentRepository.findAll();

        assertThat(students).hasSize(2);
    }

    @Test
    void shouldDeleteStudent() {
        studentRepository.deleteById(testStudent.getId());

        Optional<Student> found = studentRepository.findById(testStudent.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCheckExistsByUsername() {
        boolean exists = studentRepository.existsByUsername("student1");
        boolean notExists = studentRepository.existsByUsername("nonexistent");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void shouldGetIdAndPasswordByUsername() {
        Optional<UserInfo> userInfo = studentRepository.getIdAndPasswordByUsername("student1");

        assertThat(userInfo).isPresent();
        assertThat(userInfo.get().id()).isEqualTo(testStudent.getId());
        assertThat(userInfo.get().password()).isEqualTo("encoded_pass");
    }

    @Test
    void shouldCreateStudentWithStats() {
        Student newStudent = new Student("student_with_stats", "pass");
        studentRepository.save(newStudent);

        StudentStats stats = new StudentStats();
        stats.setUser(newStudent);
        stats.setLevel(1);
        stats.setCurrentExperience(0);
        stats.setMaxLvlExperience(100);
        statsRepository.save(stats);

        newStudent.setStats(stats);
        studentRepository.save(newStudent);

        Optional<Student> found = studentRepository.findById(newStudent.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStats()).isNotNull();
        assertThat(found.get().getStats().getLevel()).isEqualTo(1);
    }

    @Test
    void shouldUpdateStudent() {
        testStudent.setName("Updated Name");
        testStudent.setActive(false);
        studentRepository.save(testStudent);

        Student updated = studentRepository.findById(testStudent.getId()).get();
        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.isActive()).isFalse();
    }
}