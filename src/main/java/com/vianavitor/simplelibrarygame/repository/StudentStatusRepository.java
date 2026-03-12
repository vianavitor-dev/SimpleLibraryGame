package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentStatusRepository extends CrudRepository<StudentStatus, Long> {
    List<StudentStatus> findByStudent(Student student);

    List<StudentStatus> findByCurrentBook(BookReadHistory history);
}
