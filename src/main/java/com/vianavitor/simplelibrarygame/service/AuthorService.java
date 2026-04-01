package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Author;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository repository;

    private Author author;

    @Autowired
    public AuthorService() {}

    public AuthorService(AuthorRepository repository, Author author) {
        this.repository = repository;
        this.author = author;
    }

    public void add(String name) {
        repository.findByName(name)
                .ifPresent((a) -> {
                    throw new RuntimeException("author already registered");
                });

        author.setName(name);

        repository.save(author);
    }

    public Author getByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("not found author"));
    }

    public List<Author> getAll() {
        return (List<Author>) repository.findAll();
    }

    public Author get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found author"));
    }

    public List<Book> getAuthorBooks(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found author"))
                .getBooks();
    }
}
