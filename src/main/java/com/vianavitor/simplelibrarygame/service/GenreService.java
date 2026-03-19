package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    @Autowired
    private GenreRepository repository;

    public List<Genre> getAll() {
        return repository.findAll();
    }

    public Genre getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("this genre don't exists"));
    }

    public Genre getByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("this genre don't exists"));
    }
}
