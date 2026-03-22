package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Classroom;
import com.vianavitor.simplelibrarygame.model.Professor;
import com.vianavitor.simplelibrarygame.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ClassroomRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Classroom testClassroom;

    @BeforeEach
    void setUp() {
        testClassroom = new Classroom();
        testClassroom.setName("English Literature 101");
        testClassroom.setPublicCode("ENG101-2024");
        entityManager.persist(testClassroom);
        entityManager.flush();
    }

    @Test
    void shouldSaveClassroom() {
        Classroom newClassroom = new Classroom();
        newClassroom.setName("Math 101");
        newClassroom.setPublicCode("MATH101");

        Classroom saved = classroomRepository.save(newClassroom);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Math 101");
        assertThat(saved.getPublicCode()).isEqualTo("MATH101");
    }

    @Test
    void shouldFindClassroomByPublicCode() {
        Optional<Classroom> found = classroomRepository.findByPublicCode("ENG101-2024");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("English Literature 101");
    }

    @Test
    void shouldFindClassroomByName() {
        Optional<Classroom> found = classroomRepository.findByName("English Literature 101");

        assertThat(found).isPresent();
        assertThat(found.get().getPublicCode()).isEqualTo("ENG101-2024");
    }

    @Test
    void shouldReturnEmptyWhenPublicCodeNotFound() {
        Optional<Classroom> found = classroomRepository.findByPublicCode("INVALID-CODE");

        assertThat(found).isEmpty();
    }

    @Test
    void shouldEnforceUniquePublicCode() {
        Classroom duplicateClassroom = new Classroom();
        duplicateClassroom.setName("Different Name");
        duplicateClassroom.setPublicCode("ENG101-2024");

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            classroomRepository.save(duplicateClassroom);
            entityManager.flush();
        });
    }

    @Test
    void shouldEnforceUniqueName() {
        Classroom duplicateClassroom = new Classroom();
        duplicateClassroom.setName("English Literature 101");
        duplicateClassroom.setPublicCode("DIFFERENT-CODE");

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            classroomRepository.save(duplicateClassroom);
            entityManager.flush();
        });
    }

    @Test
    void shouldAddStudentsToClassroom() {
        Student student = new Student("student_in_class", "pass");
        studentRepository.save(student);

        student.getClassrooms().add(testClassroom);
        testClassroom.getUsers().add(student);

        classroomRepository.save(testClassroom);
        studentRepository.save(student);

        Classroom found = classroomRepository.findById(testClassroom.getId()).get();
        assertThat(found.getUsers()).hasSize(1);
        assertThat(found.getUsers().iterator().next().getUsername()).isEqualTo("student_in_class");
    }

    @Test
    void shouldAddProfessorToClassroom() {
        Professor professor = new Professor("prof_in_class", "pass");
        professorRepository.save(professor);

        professor.getClassrooms().add(testClassroom);
        testClassroom.getUsers().add(professor);

        classroomRepository.save(testClassroom);
        professorRepository.save(professor);

        Classroom found = classroomRepository.findById(testClassroom.getId()).get();
        assertThat(found.getUsers()).hasSize(1);
        assertThat(found.getUsers().iterator().next().getUsername()).isEqualTo("prof_in_class");
    }
}