package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Genre;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// read only
@Repository
public interface GenreRepository extends org.springframework.data.repository.Repository<Genre, Long> {
    List<Genre> findAll();

    Optional<Genre> findById(Long id);

    Optional<Genre> findByName(String name);
}
