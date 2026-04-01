package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Author;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.repository.AuthorRepository;
import com.vianavitor.simplelibrarygame.repository.BookRepository;
import com.vianavitor.simplelibrarygame.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    private static final double maxRateValue = 5;
    private static final double minRateValue = 0;

//    private static final Map<Long, Book> cache = new HashMap<>();

    public void add(Book newBook, boolean confirmed) {
        if (!confirmed) {
            boolean exists = repository.existsByTitle(newBook.getTitle());
            if (exists) {
                throw new RuntimeException("""
                    there is a book with the same title registered, do you still want to proceed?
                """);
            }
        }

        Set<Author> bookAuthors = this.getBookAuthorsHelper(newBook.getBookAuthors());
        Set<Genre> bookGenres = this.getBookGenresHelper(newBook.getBookGenres());

        newBook.setBookGenres(bookGenres);
        newBook.setBookAuthors(bookAuthors);

//         TODO: implement an IA to classify the reading difficulty
//         ...
        Book book = repository.save(newBook);
    }

    public void changeImage() {
        // TODO: make it functional
    }

    public void rate(Long id, int rate) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found book"));

        double value = book.getRatingValue();
        int count = book.getRatingCount();

        double avg = value + (rate - value)/ (count+1);

        book.setRatingCount(count + 1);
        book.setRatingValue(avg);

        repository.save(book);
    }

    public void setAvailable(Long id, boolean value) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found book"));

        book.setAvailable(value);
        repository.save(book);
    }

    public Book modify(Long id, Book data, boolean confirmed, Map<Long, Book> cache)  {
        // TODO: implement a more efficient way to stores a cache to deal with no long accessed data
        Book book = cache.containsKey(id)
                ? cache.remove(id)
                : repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found book"));

        if (!cache.containsKey(id)) {
            cache.put(id, book);
        }

        boolean isTitleFilledIn = data.getTitle() != null &&  !data.getTitle().isBlank();

//        check if the title is a duplicate
        if (isTitleFilledIn && !confirmed) {
            List<Book> results = repository.findByTitle(data.getTitle());

            if (!results.isEmpty()) {
//                 if the search returns something it means that this title already belongs to another book registered
                Optional<Book> duplicateBookTitle = results.stream()
                        .filter(b -> Objects.equals(b.getId(), id))
                        .findFirst();

                if (duplicateBookTitle.isPresent()) {
                    throw new RuntimeException("there is a book with the same title registered, even so do you wish to proceed?");
                }
            }
        }

        if (isTitleFilledIn) {
            book.setTitle(data.getTitle());
        }
        if (data.getSynopsis() != null) {
            book.setSynopsis(data.getSynopsis());
        }
        if (data.getPageCount() > 0) {
            book.setPageCount(data.getPageCount());
        }
        if (data.getReleasedAt() != null) {
            book.setReleasedAt(data.getReleasedAt());
        }
        if (data.getBookAuthors() != null) {
            Set<Author> bookAuthors = this.getBookAuthorsHelper(data.getBookAuthors());
            book.setBookAuthors(bookAuthors);
        }
        if (data.getBookGenres() != null) {
            Set<Genre> bookGenres = this.getBookGenresHelper(data.getBookGenres());
            book.setBookGenres(bookGenres);
        }

        return repository.save(book);
    }

    private Set<Author> getBookAuthorsHelper(Set<Author> bookAuthors) {
        Set<Author> result = new HashSet<>();

        bookAuthors.forEach(currentAuthor -> {
            Author author = authorRepository
                    .findByName(currentAuthor.getName())
                    .orElseGet(() ->
//                                if there aren't any currentAuthor registered with this name, then register a new one
                                    authorRepository.save(currentAuthor)
                    );
            result.add(author);
        });

        return result;
    }

    private Set<Genre> getBookGenresHelper(Set<Genre> bookGenres) {
        Set<Genre> result = new HashSet<>();

        bookGenres.forEach(currentGenre -> {
            genreRepository
                    .findByName(currentGenre.getName())
                    .ifPresent((genre) -> {
                        result.add(genre);
                    });
        });

        return result;
    }
}
