package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// TODO: implements auxiliary classes that gives support to the bidirectional relationship
@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
    Optional<Author> findByName(String name);
}
