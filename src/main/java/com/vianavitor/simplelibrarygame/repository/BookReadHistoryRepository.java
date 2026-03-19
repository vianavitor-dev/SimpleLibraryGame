package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReadHistoryRepository extends CrudRepository<BookReadHistory, Long> {
    List<BookReadHistory> findByBook(Book book);

    List<BookReadHistory> findByStudent(Student student);
}
