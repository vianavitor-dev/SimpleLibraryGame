package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Classroom;
import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import com.vianavitor.simplelibrarygame.repository.ClassroomRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.repository.StudentStatsRepository;
import com.vianavitor.simplelibrarygame.service.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class StudentServiceTest extends BaseServiceTest {

    @Mock
    private StudentRepository repository;

    @Mock
    private StudentStatsRepository statsRepository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private StudentStats stats;

    @InjectMocks
    private StudentService service;

    private Student student;

    @BeforeEach
    public void init() {
        student = new Student("student_1", "passwd");
        student.setName("student_name");

        stats.setLevel(1);
        stats.setCurrentExperience(10);
        stats.setMaxLvlExperience(150);
    }

    @Test
    void testRegister() {
        Classroom classroom = new Classroom();
        classroom.setId(1L);
        classroom.setName("EN#01-2026");
        classroom.setPublicCode("1s03KJ2");

        Genre action = new Genre();
        action.setName("action");

        Set<Genre> favoriteGenres = new HashSet<>();
        favoriteGenres.add(action);

        Mockito.when(classroomRepository.findByPublicCode("1s03KJ2"))
                .thenReturn(Optional.of(classroom));

        Mockito.when(statsRepository.save(Mockito.any(StudentStats.class)))
                        .thenReturn(stats);

        Mockito.when(repository.existsByUsername("student_1"))
                .thenReturn(false);

        Mockito.when(encoder.encode("passwd"))
                .thenAnswer(x -> {
                    student.setPassword("passwd_hash");
                    return "passwd_hash";
                });

        Mockito.when(repository.save(Mockito.any(Student.class)))
                .thenAnswer(x -> {
                   stats.setUser(student);
                   student.setId(1L);

                   return student;
                });

        service.register(student, "1s03KJ2", favoriteGenres);

        assertThat(student).isNotNull();
        assertThat(student.getId()).isEqualTo(1L);
        assertThat(student.getClassrooms()).contains(classroom);
        assertThat(student.getFavoriteGenre()).contains(action);
        assertThat(student.getStats()).isNotNull();

        Mockito.verify(repository, Mockito.times(1)).existsByUsername("student_1");
        Mockito.verify(repository, Mockito.times(2)).save(student);
        Mockito.verify(statsRepository, Mockito.times(1)).save(stats);
        Mockito.verify(encoder, Mockito.times(1)).encode("passwd");
        Mockito.verify(classroomRepository, Mockito.times(1)).findByPublicCode("1s03KJ2");
    }

    @Test
    void testGetStats() {
        Long id = 1L;

        Mockito.when(repository.findById(1L))
                .thenAnswer(x -> {
                    student.setId(1L);
                    return Optional.of(student);
                });

        Mockito.when(statsRepository.findByStudent(Mockito.any(Student.class)))
                .thenReturn(Optional.of(stats));

        StudentStats result = service.getStats(id);

        assertThat(result).isNotNull();

        Mockito.verify(repository, Mockito.times(1)).findById(id);
        Mockito.verify(statsRepository, Mockito.times(1)).findByStudent(student);
    }

    @Test
    void testGetAll() {
        List<Student> list = new ArrayList<>();
        list.add(student);

        Mockito.when(repository.findAll())
                        .thenReturn(list);

        List<Student> result = service.getAll();

        assertThat(result).isNotNull();
        assertThat(result).size().isEqualTo(1);
        assertThat(result).contains(student);

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void testGetFavoriteGenres() {
        Long id = 1L;

        Genre action = new Genre();
        action.setName("action");

        Mockito.when(repository.findById(id))
                .thenAnswer(x -> {
                    Set<Genre> favoriteGenres = new HashSet<>();
                    favoriteGenres.add(action);

                    student.setFavoriteGenre(favoriteGenres);

                    return Optional.of(student);
                });

        Set<Genre> result = service.getFavoriteGenres(id);

        assertThat(result).isNotEmpty();
        assertThat(result).size().isEqualTo(1);
        assertThat(result).contains(action);

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    void testDeactivate() {
        Long id = 1L;

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(student));

        service.deactivate(id);

        assertThat(student).isNotNull();
        assertThat(student.isActive()).isFalse();

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    void testActivate() {
        Long id = 1L;

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(student));

        service.activate(id);

        assertThat(student).isNotNull();
        assertThat(student.isActive()).isTrue();

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }
}