package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.model.Professor;
import com.vianavitor.simplelibrarygame.repository.ProfessorRepository;
import com.vianavitor.simplelibrarygame.service.utils.ManageableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService implements ManageableUser<Professor> {
    @Autowired
    private ProfessorRepository repository;
    
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Professor register(Professor entity) {
        boolean studentExists = repository.existsByUsername(entity.getUsername());

        if (studentExists) {
            throw new RuntimeException("username already in use");
        }

        String hashedPassword = encoder.encode(entity.getPassword());
        entity.setPassword(hashedPassword);

        return repository.save(entity);
    }

    public void register(Professor newProfessor, String classroomCode) {
        this.register(newProfessor);
        // TODO: enroll student in classroom
        // ...
    }

    @Override
    public Long login(String username, String password) {
        UserInfo userInfo = repository.getIdAndPasswordByUsername(username)
                .orElseThrow(() -> new RuntimeException("invalid username or password"));

        boolean invalidPassword = encoder.matches(password, userInfo.password());

        if (invalidPassword) {
            throw new RuntimeException("invalid username or password");
        }

        boolean wasUserDeactivated = repository.getActiveById(userInfo.id());
        if (wasUserDeactivated) {
            throw new RuntimeException("this user was deactivated, talk with a professor or administrador to get more information");
        }

        // TODO: create JWT Token for authentication
        // ...

        return userInfo.id();
    }

    private List<Professor> getAll() {
        return (List<Professor>) repository.findAll();
    }

    @Override
    public void deactivate(Long id) {
        Professor student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));

        student.setActive(false);
        repository.save(student);
    }

    @Override
    public void activate(Long id) {
        Professor student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));

        student.setActive(true);
        repository.save(student);
    }
}
