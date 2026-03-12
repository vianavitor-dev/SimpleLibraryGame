package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// TODO: implements auxiliary classes that gives support to the bidirectional relationship
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

}
