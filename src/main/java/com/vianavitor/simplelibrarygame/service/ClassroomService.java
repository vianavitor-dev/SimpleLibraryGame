package com.vianavitor.simplelibrarygame.service;

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

    private String generatePublicCode() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public void create(String name) {
        boolean exists = repository.findByName(name).isPresent();
        if (exists) {
            throw new RuntimeException("this classroom already exists");
        }

        Classroom classroom = new Classroom();
        classroom.setPublicCode(this.generatePublicCode());
        classroom.setName(name);

        repository.save(classroom);
    }

    public Set<UserClassroom> modifyUsersInClassroom(Long id, Set<UserClassroom> students) {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("classroom not found"));

        classroom.setUsers(students);
        return repository.save(classroom).getUsers();
    }

    public boolean changeName(Long id, String name) {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("classroom not found"));

        boolean exists = repository.findByName(name).isPresent();
        if (exists) {
            throw new RuntimeException("this classroom already exists");
        }

        classroom.setName(name);

        repository.save(classroom);
        return true;
    }

    public void delete(Long id) {
        Classroom classroom = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("classroom not found"));

        classroom.getUsers().forEach(user -> {
            Set<Classroom> userClasses = user.getClassrooms();
            userClasses.remove(classroom);

            if (user instanceof Student) {
                studentRepository.save((Student) user);
            }
            if (user instanceof Professor) {
                professorRepository.save((Professor) user);
            }

            user.setClassrooms(userClasses);
        });

        repository.delete(classroom);
    }
}
