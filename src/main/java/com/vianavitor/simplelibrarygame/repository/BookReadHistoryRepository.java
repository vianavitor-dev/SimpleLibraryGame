package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReadHistoryRepository extends CrudRepository<BookReadHistory, Long> {
    void findByBook(Book book);

    void findByStudent(Student student);
}
