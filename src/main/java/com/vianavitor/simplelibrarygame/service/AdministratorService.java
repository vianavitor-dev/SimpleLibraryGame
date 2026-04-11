package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.exception.DuplicateResourceException;
import com.vianavitor.simplelibrarygame.exception.InvalidOperationException;
import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import com.vianavitor.simplelibrarygame.exception.UserDeactivatedException;
import com.vianavitor.simplelibrarygame.model.Administrator;
import com.vianavitor.simplelibrarygame.model.Administrator;
import com.vianavitor.simplelibrarygame.repository.AdministratorRepository;
import com.vianavitor.simplelibrarygame.service.utils.ManageableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AdministratorService implements ManageableUser<Administrator> {
    @Autowired
    private AdministratorRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Administrator register(Administrator entity) {
        boolean studentExists = repository.existsByUsername(entity.getUsername());

        if (studentExists) {
            throw new DuplicateResourceException("username already in use");
        }

        String hashedPassword = encoder.encode(entity.getPassword());
        entity.setPassword(hashedPassword);

        return repository.save(entity);
    }

    public Long login(String username, String password) throws InvalidOperationException, UserDeactivatedException{
        Administrator administrator = (Administrator) repository.findByUsername(username)
                .orElseThrow(() -> new InvalidOperationException("invalid username or password"));

        boolean invalidPassword = !encoder.matches(password, administrator.getPassword());

        if (invalidPassword) {
            throw new InvalidOperationException("invalid username or password");
        }

        boolean wasUserDeactivated = !administrator.isActive();
        if (wasUserDeactivated) {
            throw new UserDeactivatedException("this user was deactivated, talk with a administrator or administrador to get more information");
        }

        // TODO: create JWT Token for authentication
        // ...

        administrator.setLastLogin(LocalDate.now());
        repository.save(administrator);

        return administrator.getId();
    }

    @Override
    public void deactivate(Long id) {
        Administrator administrator = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        administrator.setActive(false);
        repository.save(administrator);
    }

    @Override
    public void activate(Long id) {
        Administrator administrator = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        administrator.setActive(true);
        repository.save(administrator);
    }

}
