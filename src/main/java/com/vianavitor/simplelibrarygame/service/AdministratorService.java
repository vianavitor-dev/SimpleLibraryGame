package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.exception.InvalidOperationException;
import com.vianavitor.simplelibrarygame.exception.UserDeactivatedException;
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

    public Long login(String username, String password) throws InvalidOperationException, UserDeactivatedException{
        Administrator administrator = (Administrator) repository.findByUsername(username)
                .orElseThrow(() -> new InvalidOperationException("invalid username or password"));

        boolean invalidPassword = !encoder.matches(password, administrator.getPassword());

        if (invalidPassword) {
            throw new InvalidOperationException("invalid username or password");
        }

        boolean wasUserDeactivated = !administrator.isActive();
        if (wasUserDeactivated) {
            throw new UserDeactivatedException("this user was deactivated, talk with a professor or administrador to get more information");
        }

        // TODO: create JWT Token for authentication
        // ...

        administrator.setLastLogin(LocalDate.now());
        repository.save(administrator);

        return administrator.getId();
    }

}
