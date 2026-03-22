package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    boolean existsByTitle(String title);

    // TODO: make it search for similar titles instead of the exactly title
    List<Book> findByTitle(String title);
}
