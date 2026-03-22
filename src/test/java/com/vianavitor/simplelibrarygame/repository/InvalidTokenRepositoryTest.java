package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.InvalidToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidTokenRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private InvalidTokenRepository invalidTokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    private InvalidToken testToken;

    @BeforeEach
    void setUp() {
        testToken = new InvalidToken();
        testToken.setToken("abc123token");
        testToken.setExpireAt(LocalDate.now().plusDays(7));
        invalidTokenRepository.save(testToken);
    }

    @Test
    void shouldSaveInvalidToken() {
        InvalidToken newToken = new InvalidToken();
        newToken.setToken("new_token_456");
        newToken.setExpireAt(LocalDate.now().plusDays(1));

        invalidTokenRepository.save(newToken);

        boolean exists = invalidTokenRepository.existsByToken("new_token_456");
        assertThat(exists).isTrue();
    }

    @Test
    void shouldCheckExistsByToken() {
        boolean exists = invalidTokenRepository.existsByToken("abc123token");
        boolean notExists = invalidTokenRepository.existsByToken("nonexistent");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void shouldHandleExpiredTokens() {
        InvalidToken expiredToken = new InvalidToken();
        expiredToken.setToken("expired_token");
        expiredToken.setExpireAt(LocalDate.now().minusDays(1));
        invalidTokenRepository.save(expiredToken);

        boolean exists = invalidTokenRepository.existsByToken("expired_token");
        assertThat(exists).isTrue(); // Repository doesn't auto-filter expired tokens
    }

    @Test
    void shouldUpdateTokenExpiration() {
        testToken.setExpireAt(LocalDate.now().plusDays(30));
        invalidTokenRepository.save(testToken);

        boolean exists = invalidTokenRepository.existsByToken("abc123token");
        assertThat(exists).isTrue();
    }

    @Test
    void shouldHandleMultipleTokens() {
        InvalidToken token2 = new InvalidToken();
        token2.setToken("token2");
        token2.setExpireAt(LocalDate.now().plusDays(1));
        invalidTokenRepository.save(token2);

        assertThat(invalidTokenRepository.existsByToken("abc123token")).isTrue();
        assertThat(invalidTokenRepository.existsByToken("token2")).isTrue();
    }
}