package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.utils.UserIdAndHashPasswd;
import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.repository.StudentStatsRepository;
import com.vianavitor.simplelibrarygame.service.utils.ManageableUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class StudentService implements ManageableUser {
    @Autowired
    private StudentRepository repository;

    @Autowired
    private StudentStatsRepository statusRepository;

    @Autowired
    PasswordEncoder encoder;

    public void register(Student newStudent, String classroomCode, Set<Genre> favoriteGenres) {
        boolean studentExists = repository.existsByUsername(newStudent.getUsername());

        if (studentExists) {
            throw new RuntimeException("username already in use");
        }

        String hashedPassword = encoder.encode(newStudent.getPassword());
        newStudent.setPassword(hashedPassword);

        repository.save(newStudent);
        // TODO: check if student stats exists, else create it
        // ...

        // TODO: insert student's favorite genre
        // ...0

        // TODO: enroll student in classroom
        // ...
    }

    @Override
    public Long login(String username, String password) {
        UserIdAndHashPasswd idAndPassword = repository.getIdAndPasswordByUsername(username)
                .orElseThrow(() -> new RuntimeException("invalid username or password"));

        boolean invalidPassword = encoder.matches(password, idAndPassword.password());

        if (invalidPassword) {
            throw new RuntimeException("invalid username or password");
        }

        // TODO: create JWT Token for authentication
        // ...

        return idAndPassword.id();
    }

    public StudentStats getStats(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));

        Optional<StudentStats> optionalStudentStats = statusRepository.findByStudent(student);

        return optionalStudentStats.orElseGet(() -> {
            // TODO: create and return the student stats
            return null;
        });
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
