package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.InvalidToken;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidTokenRepository extends org.springframework.data.repository.Repository<InvalidToken, Long> {
    boolean existsByToken(String token);

    void save(InvalidToken token);
}
