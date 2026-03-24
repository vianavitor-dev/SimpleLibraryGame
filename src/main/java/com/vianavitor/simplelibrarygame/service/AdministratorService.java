package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.model.Administrator;
import com.vianavitor.simplelibrarygame.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AdministratorService {
    @Autowired
    private AdministratorRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public Long login(String username, String password) {
        Administrator administrator = (Administrator) repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("invalid username or password"));

        boolean invalidPassword = !encoder.matches(password, administrator.getPassword());

        if (invalidPassword) {
            throw new RuntimeException("invalid username or password");
        }

        boolean wasUserDeactivated = !administrator.isActive();
        if (wasUserDeactivated) {
            throw new RuntimeException("this user was deactivated, talk with a professor or administrador to get more information");
        }

        // TODO: create JWT Token for authentication
        // ...

        administrator.setLastLogin(LocalDate.now());
        repository.save(administrator);

        return administrator.getId();
    }

}
