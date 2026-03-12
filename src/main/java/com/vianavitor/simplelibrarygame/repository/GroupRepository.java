package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Group;
import com.vianavitor.simplelibrarygame.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
    List<Group> findByStudent(Student student);
}
