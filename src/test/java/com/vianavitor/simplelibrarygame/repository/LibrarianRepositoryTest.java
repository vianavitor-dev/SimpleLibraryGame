package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.model.Librarian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LibrarianRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Librarian testLibrarian;

    @BeforeEach
    void setUp() {
        testLibrarian = new Librarian();
        testLibrarian.setUsername("librarian1");
        testLibrarian.setPassword("encoded_pass");
        testLibrarian.setName("John Librarian");
        entityManager.persist(testLibrarian);
        entityManager.flush();
    }

    @Test
    void shouldSaveLibrarian() {
        Librarian newLibrarian = new Librarian("new_librarian", "new_pass");
        newLibrarian.setName("Jane Librarian");

        Librarian saved = librarianRepository.save(newLibrarian);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("new_librarian");
        assertThat(saved.isActive()).isTrue();
    }

    @Test
    void shouldFindById() {
        Optional<Librarian> found = librarianRepository.findById(testLibrarian.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("librarian1");
    }

    @Test
    void shouldFindAll() {
        Librarian librarian2 = new Librarian();
        librarian2.setUsername("librarian2");
        librarian2.setPassword("pass2");
        librarianRepository.save(librarian2);

        Iterable<Librarian> librarians = librarianRepository.findAll();

        assertThat(librarians).hasSize(2);
    }

    @Test
    void shouldDeleteLibrarian() {
        librarianRepository.deleteById(testLibrarian.getId());

        Optional<Librarian> found = librarianRepository.findById(testLibrarian.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCheckExistsByUsername() {
        boolean exists = librarianRepository.existsByUsername("librarian1");
        boolean notExists = librarianRepository.existsByUsername("nonexistent");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void shouldGetIdAndPasswordByUsername() {
        Optional<UserInfo> userInfo = librarianRepository.getIdAndPasswordByUsername("librarian1");

        assertThat(userInfo).isPresent();
        assertThat(userInfo.get().id()).isEqualTo(testLibrarian.getId());
        assertThat(userInfo.get().password()).isEqualTo("encoded_pass");
    }

    @Test
    void shouldUpdateLibrarian() {
        testLibrarian.setName("Updated Name");
        testLibrarian.setActive(false);
        librarianRepository.save(testLibrarian);

        Librarian updated = librarianRepository.findById(testLibrarian.getId()).get();
        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.isActive()).isFalse();
    }
}