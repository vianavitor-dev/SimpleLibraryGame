package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Author;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = new Author();
        testAuthor.setName("J.K. Rowling");
        entityManager.persist(testAuthor);
        entityManager.flush();
    }

    @Test
    void shouldSaveAuthor() {
        Author newAuthor = new Author();
        newAuthor.setName("George Orwell");

        Author saved = authorRepository.save(newAuthor);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("George Orwell");
    }

    @Test
    void shouldFindAuthorByName() {
        Optional<Author> found = authorRepository.findByName("J.K. Rowling");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(testAuthor.getId());
    }

    @Test
    void shouldReturnEmptyWhenAuthorNotFound() {
        Optional<Author> found = authorRepository.findByName("Nonexistent Author");

        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindById() {
        Optional<Author> found = authorRepository.findById(testAuthor.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("J.K. Rowling");
    }

    @Test
    void shouldHandleAuthorWithBooks() {
        Book book = createBook("Harry Potter");
        book.getBookAuthors().add(testAuthor);
        testAuthor.getBooks().add(book);

        bookRepository.save(book);
        authorRepository.save(testAuthor);

        Optional<Author> found = authorRepository.findById(testAuthor.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getBooks()).hasSize(1);
        assertThat(found.get().getBooks().get(0).getTitle()).isEqualTo("Harry Potter");
    }

    @Test
    void shouldEnforceUniqueAuthorName() {
        Author duplicateAuthor = new Author();
        duplicateAuthor.setName("J.K. Rowling");

        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            authorRepository.save(duplicateAuthor);
            entityManager.flush();
        });
    }

    private Book createBook(String title) {
        Book book = new Book();
        book.setTitle(title);
        book.setReleasedAt(LocalDate.now());
        book.setDifficultLevel(ReadingLevel.EASY);
        book.setAvailable(true);
        book.setPageCount(300);
        return book;
    }
}