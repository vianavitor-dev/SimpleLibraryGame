package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import com.vianavitor.simplelibrarygame.model.Group;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.repository.GroupRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepository repository;

    @Autowired
    private StudentRepository studentRepository;

    public Group create(String name, Long studentId) throws ResourceNotFoundException {
        Student owner = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("not found student"));

        Group group = new Group();
        group.setName(name);
        group.setStudent(owner);

        return repository.save(group);
    }

    public Group changeName(Long id, String name) throws ResourceNotFoundException {
        Group group = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found group"));

        group.setName(name);
        return repository.save(group);
    }

    public List<Group> getByStudent(Long studentId) throws ResourceNotFoundException {
        Student owner = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("not found student"));

        return repository.findByStudent(owner);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
