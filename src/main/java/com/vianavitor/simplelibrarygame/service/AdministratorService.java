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
