package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentStatsRepository extends CrudRepository<StudentStats, Long> {
    Optional<StudentStats> findByStudent(Student student);

    List<StudentStats> findByCurrentBook(BookReadHistory history);
}
