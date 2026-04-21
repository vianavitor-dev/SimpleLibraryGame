package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.dto.utils.UserInfo;
import com.vianavitor.simplelibrarygame.exception.DuplicateResourceException;
import com.vianavitor.simplelibrarygame.exception.InvalidOperationException;
import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import com.vianavitor.simplelibrarygame.exception.UserDeactivatedException;
import com.vianavitor.simplelibrarygame.model.Classroom;
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

import java.time.LocalDate;
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
    public Student register(Student entity) throws DuplicateResourceException {
        boolean studentExists = repository.existsByUsername(entity.getUsername());

        if (studentExists) {
            throw new DuplicateResourceException("username already in use");
        }

        String hashedPassword = encoder.encode(entity.getPassword());
        entity.setPassword(hashedPassword);

        return repository.save(entity);
    }

    public void register(Student newStudent, String classroomCode, Set<Genre> favoriteGenres) throws DuplicateResourceException, ResourceNotFoundException {
        Classroom classroom = classroomRepository.findByPublicCode(classroomCode)
                .orElseThrow(() -> new ResourceNotFoundException("not found classroom"));

        newStudent.setFavoriteGenre(favoriteGenres);
        Student student = this.register(newStudent);

        StudentStats stats = new StudentStats();
        stats.setUser(student);
        stats.setLevel(1);
        stats.setMaxLvlExperience(150);
        stats = statsRepository.save(stats);

        classroom.getUsersInClassroom().add(student);
        classroomRepository.save(classroom);

        student.setStats(stats);
        student.getClassrooms().add(classroom);
        repository.save(student);
    }

    public StudentStats getStats(Long id) throws ResourceNotFoundException {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        return statsRepository.findByStudent(student)
                .orElseThrow(() -> new ResourceNotFoundException("not found student stats"));
    }

    public List<Student> getAll() {
        return (List<Student>) repository.findAll();
    }


    public Set<Genre> getFavoriteGenres(Long id) throws ResourceNotFoundException {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        return student.getFavoriteGenre();
    }

    @Override
    public void deactivate(Long id) throws ResourceNotFoundException {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        student.setActive(false);
        repository.save(student);
    }

    @Override
    public void activate(Long id) throws ResourceNotFoundException {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        student.setActive(true);
        repository.save(student);
    }
}
