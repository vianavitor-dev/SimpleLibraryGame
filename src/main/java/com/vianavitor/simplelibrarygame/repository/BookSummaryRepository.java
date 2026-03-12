package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookSummary;
import com.vianavitor.simplelibrarygame.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSummaryRepository extends CrudRepository<BookSummary, Long> {
    List<BookSummary> findByStudent(Student student);

    List<BookSummary> findByBook(Book book);
}
