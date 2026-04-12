package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Classroom;
import com.vianavitor.simplelibrarygame.model.Librarian;
import com.vianavitor.simplelibrarygame.model.Professor;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.utils.classes.UserClassroom;
import com.vianavitor.simplelibrarygame.repository.ClassroomRepository;
import com.vianavitor.simplelibrarygame.repository.ProfessorRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.repository.UserRepository;
import com.vianavitor.simplelibrarygame.service.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassroomServiceTest extends BaseServiceTest {
    @Mock
    private ClassroomRepository repository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    private Classroom testClassroom;

    @BeforeEach
    void setUp() {
        testClassroom = new Classroom();
        testClassroom.setName("English Literature 101");
        testClassroom.setPublicCode("ENG101-2024");
    }

    @Test
    public void testCreate() {
        Classroom classroom = new Classroom();

        when(repository.findByName("Portuguese Literature 021"))
                .thenReturn(Optional.empty());

        when(repository.save(classroom))
                .thenReturn(null);

        ClassroomService service = new ClassroomService(
                repository, professorRepository, studentRepository, classroom
        );

        service.create("Portuguese Literature 021");

        System.out.println("public code: " + classroom.getPublicCode());

        assertThat(classroom.getName()).isEqualTo("Portuguese Literature 021");
        assertThat(classroom.getPublicCode()).isNotBlank();

        verify(repository, times(1)).findByName("Portuguese Literature 021");
        verify(repository, times(1)).save(classroom);
    }

    @Test
    public void testCreateDuplicateNameException() {
        Classroom classroom = new Classroom();
        classroom.setName("Portuguese Literature 021");
        classroom.setPublicCode("LP021-2026");

        when(repository.findByName("Portuguese Literature 021"))
                .thenReturn(Optional.of(classroom));

        ClassroomService service = new ClassroomService(
                repository, professorRepository, studentRepository, classroom
        );

        assertThrows(Exception.class, () -> {
            service.create("Portuguese Literature 021");
        });

        verify(repository, times(1)).findByName("Portuguese Literature 021");
    }

    @Test
    public void testModifyUsersInClassroom() {
        UserClassroom student = new Student("student1", "123");
        UserClassroom professor = new Professor("professor", "098");

        Set<Long> IDs = new HashSet<>();
        IDs.add(1L);
        IDs.add(2L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(testClassroom));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(professor));

        when(repository.save(testClassroom))
                .thenReturn(testClassroom);

        ClassroomService service = new ClassroomService(
                repository, professorRepository, studentRepository, userRepository, testClassroom
        );

        Set<UserClassroom> result = service.modifyUsersInClassroom(1L, IDs);

        assertThat(result).isNotEmpty();
        assertThat(result).contains(student).contains(professor);

        verify(repository, times(1)).findById(1L);
        verify(userRepository, times(2)).findById(any(Long.class));
        verify(repository, times(1)).save(testClassroom);
    }

    @Test
    public void testModifyUsersInClassroomInvalidArgException() {
        Librarian librarian = new Librarian("librarian1", "123");

        Set<Long> IDs = new HashSet<>();
        IDs.add(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(testClassroom));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(librarian));

        ClassroomService service = new ClassroomService(
                repository, professorRepository, studentRepository, userRepository, testClassroom
        );

        assertThrows(IllegalArgumentException.class, () -> {
            service.modifyUsersInClassroom(1L, IDs);
        });

        verify(repository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
    }
}
