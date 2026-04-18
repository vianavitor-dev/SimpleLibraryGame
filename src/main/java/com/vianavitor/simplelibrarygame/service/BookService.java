package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.exception.DuplicateResourceException;
import com.vianavitor.simplelibrarygame.exception.ResourceNotFoundException;
import com.vianavitor.simplelibrarygame.exception.UnconfirmedOperationException;
import com.vianavitor.simplelibrarygame.exception.UnsupportedFileTypeException;
import com.vianavitor.simplelibrarygame.model.Author;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import com.vianavitor.simplelibrarygame.repository.AuthorRepository;
import com.vianavitor.simplelibrarygame.repository.BookRepository;
import com.vianavitor.simplelibrarygame.repository.GenreRepository;
import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Value("${book.image.path}")
    private String imagePath;

    @Autowired
    public BookService() {}

    public BookService(
            BookRepository repository, AuthorRepository authorRepository,
            GenreRepository genreRepository, String imagePath
    ) {
        this.repository = repository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.imagePath = imagePath;
    }

    private ReadingLevel getDifficultLevel(int pages) {
        if (pages < 100) {
            return ReadingLevel.EASY;
        }
        else if (pages <= 200) {
            return ReadingLevel.MEDIUM;
        }

        return ReadingLevel.HARD;
    }

    public void add(Book newBook, boolean confirmed) throws UnconfirmedOperationException {
        if (!confirmed) {
            boolean exists = repository.existsByTitle(newBook.getTitle());
            if (exists) {
                throw new UnconfirmedOperationException("there is a book with the same title registered, do you still want to proceed?");
            }
        }

        Set<Author> bookAuthors = this.getBookAuthorsHelper(newBook.getBookAuthors());
        Set<Genre> bookGenres = this.getBookGenresHelper(newBook.getBookGenres());

        newBook.setBookGenres(bookGenres);
        newBook.setBookAuthors(bookAuthors);
        newBook.setAvailable(true);
        newBook.setQuantity(1);

//         TODO: implement an IA to classify the reading difficulty
//         ...
        newBook.setDifficultLevel(this.getDifficultLevel(newBook.getPageCount()));

        Book book = repository.save(newBook);
    }

    public List<Book> getAllBooks() {
        return (List<Book>) repository.findAll();
    }

    public Book getBook(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found book"));
    }

    public Book changeImage(Long id, MultipartFile file) throws ResourceNotFoundException, IOException {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found book"));

        File directory = new File(imagePath + "/");
        File destination = getFile(file, directory, book);
        file.transferTo(destination);

//        System.out.println(destination.getAbsolutePath());

        book.setImagePath(destination.getAbsolutePath());
        return repository.save(book);
    }

    private static @NonNull File getFile(MultipartFile file, File directory, Book book) throws UnsupportedFileTypeException {
        if (!directory.exists()) {
            directory.mkdirs(); // Ensure directory exists
        }

        if (file.getContentType() == null) {
            throw new NullPointerException("file content type cannot be null");
        }

        String splited[] = file.getContentType().split("/");

        if (splited.length <= 1) {
            throw new UnsupportedFileTypeException("invalid file content type format");
        }

        String extension = splited[1];

        switch (extension) {
            case "jpeg", "png", "gif", "bmp":
                break;
            default:
                throw new UnsupportedFileTypeException("unsupported image type: " + extension);
        }

        return new File(directory, book.getId().toString() + "." + extension);
    }

    public void rate(Long id, int rate) throws ResourceNotFoundException{
        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found book"));

        double value = book.getRatingValue();
        int count = book.getRatingCount();

        double avg = value + (rate - value)/ (count+1);

        book.setRatingCount(count + 1);
        book.setRatingValue(avg);

        repository.save(book);
    }

    public void setAvailable(Long id, boolean value) throws ResourceNotFoundException {
        Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found book"));

        book.setAvailable(value);
        repository.save(book);
    }

    public Book modify(Long id, Book data, boolean confirmed, Map<Long, Book> cache) throws ResourceNotFoundException, DuplicateResourceException {
        // TODO: implement a more efficient way to stores a cache to deal with no long accessed data
        Book book = cache.containsKey(id)
                ? cache.remove(id)
                : repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("not found book"));

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
                    throw new DuplicateResourceException("there is a book with the same title registered, even so do you wish to proceed?");
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
