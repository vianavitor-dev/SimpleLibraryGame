package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.response.UserInClassroomResponse;
import com.vianavitor.simplelibrarygame.exception.DuplicateResourceException;
import com.vianavitor.simplelibrarygame.exception.InvalidOperationException;
import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import com.vianavitor.simplelibrarygame.model.*;
import com.vianavitor.simplelibrarygame.model.utils.classes.User;
import com.vianavitor.simplelibrarygame.model.utils.classes.UserClassroom;
import com.vianavitor.simplelibrarygame.repository.ClassroomRepository;
import com.vianavitor.simplelibrarygame.repository.ProfessorRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository repository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    private String generatePublicCode() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public void create(String name) throws DuplicateResourceException {
        boolean exists = repository.findByName(name).isPresent();
        if (exists) {
            throw new DuplicateResourceException("this classroom already exists");
        }

        Classroom classroom = new Classroom();
        classroom.setPublicCode(this.generatePublicCode());
        classroom.setName(name);

        repository.save(classroom);
    }

    public List<Classroom> getAllClasses() {
        return (List<Classroom>) repository.findAll();
    }

    public Classroom getClassroom(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("classroom not found"));
    }

    public Set<UserClassroom> modifyUsersInClassroom(Long id, Set<Long> userIds) throws ResourceNotFoundException, IllegalArgumentException {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("classroom not found"));

        Set<UserClassroom> classroomUsers = new HashSet<>();
        Iterator<Long> iterator = userIds.iterator();
        Long current = iterator.next();
        
        while (current != null) {
            User user = userRepository.findById(current)
                    .orElseThrow(() -> new ResourceNotFoundException("classroom not found"));

            if (!(user instanceof Professor) && !(user instanceof Student)) {
                throw new IllegalArgumentException("non supported user type: " + user.getClass().getSimpleName());
            }

            classroomUsers.add((UserClassroom) user);

            if (!iterator.hasNext()) break;

            current = iterator.next();
        }
        
        classroom.setUsersInClassroom(classroomUsers);
        return repository.save(classroom).getUsersInClassroom();
    }

    public Set<UserClassroom> getUsersInClassroom(Long id) throws ResourceNotFoundException, IllegalArgumentException {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("classroom not found"));

        return classroom.getUsersInClassroom();
    }

    public Set<Classroom> getByProfessor(Long professorId) throws ResourceNotFoundException {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("professor not found"));

        return professor.getClassrooms();
    }

    public Classroom changeName(Long id, String name) throws ResourceNotFoundException, DuplicateResourceException {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("classroom not found"));

        boolean exists = repository.findByName(name).isPresent();
        if (exists) {
            throw new DuplicateResourceException("a classroom with this name already exists");
        }

        classroom.setName(name);

        return repository.save(classroom);
    }

    public void delete(Long id) throws ResourceNotFoundException {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("classroom not found"));

        repository.delete(classroom);
    }
}
