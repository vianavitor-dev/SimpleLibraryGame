package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.exception.DuplicateResourceException;
import com.vianavitor.simplelibrarygame.exception.InvalidOperationException;
import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import com.vianavitor.simplelibrarygame.exception.UserDeactivatedException;
import com.vianavitor.simplelibrarygame.model.Librarian;
import com.vianavitor.simplelibrarygame.repository.LibrarianRepository;
import com.vianavitor.simplelibrarygame.service.utils.ManageableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LibrarianService implements ManageableUser<Librarian> {
    @Autowired
    private LibrarianRepository repository;
    
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Librarian register(Librarian entity) throws DuplicateResourceException {
        boolean studentExists = repository.existsByUsername(entity.getUsername());

        if (studentExists) {
            throw new DuplicateResourceException("username already in use");
        }

        String hashedPassword = encoder.encode(entity.getPassword());
        entity.setPassword(hashedPassword);

        return repository.save(entity);
    }

    public List<Librarian> getAll() {
        return (List<Librarian>) repository.findAll();
    }

    @Override
    public void deactivate(Long id) throws ResourceNotFoundException {
        Librarian student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        student.setActive(false);
        repository.save(student);
    }

    @Override
    public void activate(Long id) throws ResourceNotFoundException {
        Librarian student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        student.setActive(true);
        repository.save(student);
    }
}
