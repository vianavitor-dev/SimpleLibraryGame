package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.exception.DuplicateResourceException;
import com.vianavitor.simplelibrarygame.exception.InvalidOperationException;
import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import com.vianavitor.simplelibrarygame.exception.UserDeactivatedException;
import com.vianavitor.simplelibrarygame.model.Classroom;
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
        Classroom classroom = classroomRepository.findByPublicCode(classroomCode)
                        .orElseThrow(() -> new ResourceNotFoundException("not found classroom"));

        Professor professor = this.register(newProfessor);
        classroom.getUsers().add(professor);

        classroomRepository.save(classroom);
//        newProfessor.getClassrooms().add(classroom);
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
