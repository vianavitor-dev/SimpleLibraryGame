package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GenreRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Genre testGenre;

    @BeforeEach
    void setUp() {
        testGenre = new Genre();
        testGenre.setName("Science Fiction");
        entityManager.persist(testGenre);
        entityManager.flush();
    }

    @Test
    void shouldSaveGenre() {
        Genre newGenre = new Genre();
        newGenre.setName("Mystery");

        entityManager.persist(newGenre);
        entityManager.flush();

        Optional<Genre> found = genreRepository.findByName("Mystery");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mystery");
    }

    @Test
    void shouldFindAllGenres() {
        Genre genre2 = new Genre();
        genre2.setName("Fantasy");
        entityManager.persist(genre2);
        entityManager.flush();

        List<Genre> genres = genreRepository.findAll();

        assertThat(genres).hasSize(2);
        assertThat(genres).extracting(Genre::getName)
                .containsExactlyInAnyOrder("Science Fiction", "Fantasy");
    }

    @Test
    void shouldFindGenreById() {
        Optional<Genre> found = genreRepository.findById(testGenre.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Science Fiction");
    }

    @Test
    void shouldFindGenreByName() {
        Optional<Genre> found = genreRepository.findByName("Science Fiction");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(testGenre.getId());
    }

    @Test
    void shouldReturnEmptyWhenGenreNotFound() {
        Optional<Genre> found = genreRepository.findByName("Nonexistent");

        assertThat(found).isEmpty();
    }

    @Test
    void shouldEnforceUniqueGenreName() {
        Genre duplicateGenre = new Genre();
        duplicateGenre.setName("Science Fiction");

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            entityManager.persist(duplicateGenre);
            entityManager.flush();
        });
    }

    @Test
    void shouldHandleStudentFavoriteGenres() {
        Student student = new Student("genre_lover", "pass");
        studentRepository.save(student);

        student.getFavoriteGenre().add(testGenre);
        testGenre.getStudents().add(student);

        studentRepository.save(student);
        entityManager.persist(testGenre);
        entityManager.flush();

        Optional<Genre> found = genreRepository.findById(testGenre.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStudents()).hasSize(1);
        assertThat(found.get().getStudents().iterator().next().getUsername()).isEqualTo("genre_lover");
    }
}