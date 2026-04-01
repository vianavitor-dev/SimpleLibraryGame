package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Classroom;
import com.vianavitor.simplelibrarygame.repository.ClassroomRepository;
import com.vianavitor.simplelibrarygame.repository.ProfessorRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.service.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

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
}
