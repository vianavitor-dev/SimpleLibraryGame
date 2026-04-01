package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.Classroom;
import com.vianavitor.simplelibrarygame.model.Professor;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.utils.classes.UserClassroom;
import com.vianavitor.simplelibrarygame.repository.ClassroomRepository;
import com.vianavitor.simplelibrarygame.repository.ProfessorRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository repository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Classroom classroom;

    @Autowired
    public ClassroomService() { }

    public ClassroomService(
            ClassroomRepository repository, ProfessorRepository professorRepository,
            StudentRepository studentRepository, Classroom classroom
    ) {
        this.repository = repository;
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
        this.classroom = classroom;
    }

    private String generatePublicCode() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public void create(String name) {
        boolean exists = repository.findByName(name).isPresent();
        if (exists) {
            throw new RuntimeException("this classroom already exists");
        }

        classroom.setPublicCode(this.generatePublicCode());
        classroom.setName(name);

        repository.save(classroom);
    }

    public Set<UserClassroom> modifyUsersInClassroom(Long id, Set<UserClassroom> students) {
        classroom = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("classroom not found"));

        classroom.setUsers(students);
        return repository.save(classroom).getUsers();
    }

    public Classroom changeName(Long id, String name) {
        classroom = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("classroom not found"));

        boolean exists = repository.findByName(name).isPresent();
        if (exists) {
            throw new RuntimeException("this classroom already exists");
        }

        classroom.setName(name);

        return repository.save(classroom);
    }

    public void delete(Long id) {
        classroom = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("classroom not found"));

        repository.delete(classroom);
    }
}
