package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Classroom;
import com.vianavitor.simplelibrarygame.model.Professor;
import com.vianavitor.simplelibrarygame.repository.ClassroomRepository;
import com.vianavitor.simplelibrarygame.repository.ProfessorRepository;
import com.vianavitor.simplelibrarygame.service.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfessorServiceTest extends BaseServiceTest {
    @Mock
    private ProfessorRepository repository;
    
    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private ProfessorService service;

    private Professor professor;

    @BeforeEach
    public void init() {
        professor = new Professor("professor_1", "passwd");
        professor.setName("professor_name");
    }

    @Test
    void testRegister() {
        Classroom classroom = new Classroom();
        classroom.setId(1L);
        classroom.setName("EN#01-2026");
        classroom.setPublicCode("1s03KJ2");

        Mockito.when(classroomRepository.findByPublicCode("1s03KJ2"))
                .thenReturn(Optional.of(classroom));

        Mockito.when(repository.existsByUsername("professor_1"))
                .thenReturn(false);

        Mockito.when(encoder.encode("passwd"))
                .thenAnswer(x -> {
                    professor.setPassword("passwd_hash");
                    return "passwd_hash";
                });

        Mockito.when(repository.save(Mockito.any(Professor.class)))
                .thenAnswer(x -> {
                    professor.setId(1L);

                    return professor;
                });

        service.register(professor, "1s03KJ2");

        assertThat(professor).isNotNull();
        assertThat(professor.getId()).isEqualTo(1L);
        assertThat(professor.getClassrooms()).contains(classroom);

        Mockito.verify(repository, Mockito.times(1)).existsByUsername("professor_1");
        Mockito.verify(repository, Mockito.times(1)).save(professor);
        Mockito.verify(encoder, Mockito.times(1)).encode("passwd");
        Mockito.verify(classroomRepository, Mockito.times(1)).findByPublicCode("1s03KJ2");
    }

    @Test
    void testGetAll() {
        List<Professor> list = new ArrayList<>();
        list.add(professor);

        Mockito.when(repository.findAll())
                .thenReturn(list);

        List<Professor> result = service.getAll();

        assertThat(result).isNotNull();
        assertThat(result).size().isEqualTo(1);
        assertThat(result).contains(professor);

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void testDeactivate() {
        Long id = 1L;

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(professor));

        service.deactivate(id);

        assertThat(professor).isNotNull();
        assertThat(professor.isActive()).isFalse();

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    void testActivate() {
        Long id = 1L;

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(professor));

        service.activate(id);

        assertThat(professor).isNotNull();
        assertThat(professor.isActive()).isTrue();

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }
}
