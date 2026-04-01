package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.InvalidToken;
import com.vianavitor.simplelibrarygame.repository.InvalidTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class InvalidTokenService {
    @Autowired
    private InvalidTokenRepository repository;

    private InvalidToken invalidToken;

    public void add(String token) {
        boolean exists = repository.existsByToken(token);

        if (exists) {
            return;
        }

        LocalDate now = LocalDate.now();
        long daysUntilDeleteDate = 1; // 1 day is just a value for test purposes

        LocalDate deleteDate = ChronoUnit.DAYS.addTo(now, daysUntilDeleteDate);

        invalidToken.setToken(token);
        invalidToken.setExpireAt(deleteDate);

        repository.save(invalidToken);
    }

    public boolean isTokenValid(String token) {
        return !repository.existsByToken(token);
    }

    // These tokens are going to be deleted from the database by a script
}
