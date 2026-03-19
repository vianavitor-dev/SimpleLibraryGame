package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import com.vianavitor.simplelibrarygame.repository.ClassroomRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.repository.StudentStatsRepository;
import com.vianavitor.simplelibrarygame.service.utils.ManageableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class StudentService implements ManageableUser<Student> {
    @Autowired
    private StudentRepository repository;

    @Autowired
    private StudentStatsRepository statsRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Student register(Student entity) {
        boolean studentExists = repository.existsByUsername(entity.getUsername());

        if (studentExists) {
            throw new RuntimeException("username already in use");
        }

        String hashedPassword = encoder.encode(entity.getPassword());
        entity.setPassword(hashedPassword);

        return repository.save(entity);
    }

    public void register(Student newStudent, String classroomCode, Set<Genre> favoriteGenres) {
        classroomRepository.findByPublicCode(classroomCode)
                .ifPresentOrElse((classroom ->
                        newStudent.getClassrooms().add(classroom)
                ), () -> {
                    throw new RuntimeException("not found classroom");
                });

        newStudent.setFavoriteGenre(favoriteGenres);

        Student student = this.register(newStudent);

//        boolean exists = statsRepository.existsById(student.getId());
//        if (exists) {
//            throw new RuntimeException("student stats already exists");
//        }
//
        StudentStats stats = new StudentStats();
        statsRepository.save(stats);

    }

    public StudentStats getStats(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));

        return statsRepository.findByStudent(student)
                .orElseThrow(() -> new RuntimeException("not found student stats"));
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

    public List<Student> getAll() {
        return (List<Student>) repository.findAll();
    }


    public Set<Genre> getFavoriteGenres(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));

        return student.getFavoriteGenre();
    }

    @Override
    public void deactivate(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));

        student.setActive(false);
        repository.save(student);
    }

    @Override
    public void activate(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));

        student.setActive(true);
        repository.save(student);
    }
}
