package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.exception.DuplicateResourceException;
import com.vianavitor.simplelibrarygame.exception.InvalidOperationException;
import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import com.vianavitor.simplelibrarygame.exception.UserDeactivatedException;
import com.vianavitor.simplelibrarygame.model.Professor;
import com.vianavitor.simplelibrarygame.repository.ClassroomRepository;
import com.vianavitor.simplelibrarygame.repository.ProfessorRepository;
import com.vianavitor.simplelibrarygame.service.utils.ManageableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProfessorService implements ManageableUser<Professor> {
    @Autowired
    private ProfessorRepository repository;

    @Autowired
    private ClassroomRepository classroomRepository;
    
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Professor register(Professor entity) throws DuplicateResourceException {
        boolean professorExists = repository.existsByUsername(entity.getUsername());

        if (professorExists) {
            throw new DuplicateResourceException("username already in use");
        }

        String hashedPassword = encoder.encode(entity.getPassword());
        entity.setPassword(hashedPassword);

        return repository.save(entity);
    }

    public void register(Professor newProfessor, String classroomCode) throws DuplicateResourceException, ResourceNotFoundException {
        classroomRepository.findByPublicCode(classroomCode)
                .ifPresentOrElse((classroom ->
                        newProfessor.getClassrooms().add(classroom)
                ), () -> {
                    throw new ResourceNotFoundException("not found classroom");
                });

        this.register(newProfessor);
    }

    @Override
    public Long login(String username, String password) throws InvalidOperationException, UserDeactivatedException {
        Professor professor = (Professor) repository.findByUsername(username)
                .orElseThrow(() -> new InvalidOperationException("invalid username or password"));

        boolean invalidPassword = !encoder.matches(password, professor.getPassword());

        if (invalidPassword) {
            throw new InvalidOperationException("invalid username or password");
        }

        boolean wasUserDeactivated = !professor.isActive();
        if (wasUserDeactivated) {
            throw new UserDeactivatedException("this user was deactivated, talk with a professor or administrador to get more information");
        }

        // TODO: create JWT Token for authentication
        // ...

        professor.setLastLogin(LocalDate.now());
        repository.save(professor);

        return professor.getId();
    }

    public List<Professor> getAll() {
        return (List<Professor>) repository.findAll();
    }

    @Override
    public void deactivate(Long id) throws ResourceNotFoundException {
        Professor professor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        professor.setActive(false);
        repository.save(professor);
    }

    @Override
    public void activate(Long id) throws ResourceNotFoundException {
        Professor professor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        professor.setActive(true);
        repository.save(professor);
    }
}
