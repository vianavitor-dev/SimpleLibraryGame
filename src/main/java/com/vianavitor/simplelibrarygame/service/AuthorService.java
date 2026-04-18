package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.exception.DuplicateResourceException;
import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
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
    public AuthorService() {
        this.author = new Author();
    }

    public AuthorService(AuthorRepository repository, Author author) {
        this.repository = repository;
        this.author = author;
    }

    public void add(String name) throws DuplicateResourceException {
        repository.findByName(name)
                .ifPresent((a) -> {
                    throw new DuplicateResourceException("author already registered");
                });

        author = new Author();
        author.setName(name);
        repository.save(author);
    }

    public Author getByName(String name) throws ResourceNotFoundException {
        return repository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("not found author"));
    }

    public List<Author> getAll() {
        return (List<Author>) repository.findAll();
    }

    public Author get(Long id) throws ResourceNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found author"));
    }

    public List<Book> getAuthorBooks(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found author"))
                .getBooks();
    }
}
