package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessorRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Professor testProfessor;

    @BeforeEach
    void setUp() {
        testProfessor = new Professor("professor1", "encoded_pass");
        testProfessor.setName("Dr. Smith");
        entityManager.persist(testProfessor);
        entityManager.flush();
    }

    @Test
    void shouldSaveProfessor() {
        Professor newProfessor = new Professor("new_prof", "new_pass");
        newProfessor.setName("Professor Johnson");

        Professor saved = professorRepository.save(newProfessor);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("new_prof");
        assertThat(saved.getName()).isEqualTo("Professor Johnson");
        assertThat(saved.isActive()).isTrue();
    }

    @Test
    void shouldFindById() {
        Optional<Professor> found = professorRepository.findById(testProfessor.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("professor1");
    }

    @Test
    void shouldFindAll() {
        Professor professor2 = new Professor("prof2", "pass2");
        professorRepository.save(professor2);

        Iterable<Professor> professors = professorRepository.findAll();

        assertThat(professors).hasSize(2);
    }

    @Test
    void shouldDeleteProfessor() {
        professorRepository.deleteById(testProfessor.getId());

        Optional<Professor> found = professorRepository.findById(testProfessor.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCheckExistsByUsername() {
        boolean exists = professorRepository.existsByUsername("professor1");
        boolean notExists = professorRepository.existsByUsername("nonexistent");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void shouldGetIdAndPasswordByUsername() {
        Optional<UserInfo> userInfo = professorRepository.getIdAndPasswordByUsername("professor1");

        assertThat(userInfo).isPresent();
        assertThat(userInfo.get().id()).isEqualTo(testProfessor.getId());
        assertThat(userInfo.get().password()).isEqualTo("encoded_pass");
    }

    @Test
    void shouldUpdateProfessor() {
        testProfessor.setName("Dr. Smith Updated");
        testProfessor.setActive(false);
        professorRepository.save(testProfessor);

        Professor updated = professorRepository.findById(testProfessor.getId()).get();
        assertThat(updated.getName()).isEqualTo("Dr. Smith Updated");
        assertThat(updated.isActive()).isFalse();
    }
}