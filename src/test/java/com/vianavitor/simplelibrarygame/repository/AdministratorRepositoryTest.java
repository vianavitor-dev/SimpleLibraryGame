package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.model.Administrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AdministratorRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Administrator testAdmin;

    @BeforeEach
    void setUp() {
        testAdmin = new Administrator("test_admin", "encoded_password");
        testAdmin.setName("Test Administrator");
        entityManager.persist(testAdmin);
        entityManager.flush();
    }

    @Test
    void shouldSaveAdministrator() {
        Administrator newAdmin = new Administrator("new_admin", "new_password");
        newAdmin.setName("New Administrator");

        Administrator saved = administratorRepository.save(newAdmin);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("new_admin");
        assertThat(saved.isActive()).isTrue();
    }

    @Test
    void shouldFindById() {
        Optional<Administrator> found = administratorRepository.findById(testAdmin.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("test_admin");
    }

    @Test
    void shouldFindAll() {
        Administrator admin2 = new Administrator("admin2", "pass2");
        administratorRepository.save(admin2);

        Iterable<Administrator> admins = administratorRepository.findAll();

        assertThat(admins).hasSize(2);
    }

    @Test
    void shouldDeleteAdministrator() {
        administratorRepository.deleteById(testAdmin.getId());

        Optional<Administrator> found = administratorRepository.findById(testAdmin.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCheckExistsByUsername() {
        boolean exists = administratorRepository.existsByUsername("test_admin");
        boolean notExists = administratorRepository.existsByUsername("nonexistent");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void shouldGetIdAndPasswordByUsername() {
        Optional<UserInfo> userInfo = administratorRepository.getIdAndPasswordByUsername("test_admin");

        assertThat(userInfo).isPresent();
        assertThat(userInfo.get().id()).isEqualTo(testAdmin.getId());
        assertThat(userInfo.get().password()).isEqualTo("encoded_password");
    }

    @Test
    void shouldReturnEmptyWhenUsernameNotFound() {
        Optional<UserInfo> userInfo = administratorRepository.getIdAndPasswordByUsername("nonexistent");

        assertThat(userInfo).isEmpty();
    }
}