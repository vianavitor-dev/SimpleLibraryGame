package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Author;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setTitle("The Hobbit");
        testBook.setSynopsis("A fantasy novel");
        testBook.setReleasedAt(LocalDate.of(1937, 9, 21));
        testBook.setDifficultLevel(ReadingLevel.MEDIUM);
        testBook.setAvailable(true);
        testBook.setPageCount(310);
        testBook.setQuantity(5);
        entityManager.persist(testBook);
        entityManager.flush();
    }

    @Test
    void shouldSaveBook() {
        Book newBook = new Book();
        newBook.setTitle("1984");
        newBook.setReleasedAt(LocalDate.of(1949, 6, 8));
        newBook.setDifficultLevel(ReadingLevel.HARD);
        newBook.setAvailable(true);
        newBook.setPageCount(328);

        Book saved = bookRepository.save(newBook);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("1984");
        assertThat(saved.getRatingValue()).isEqualTo(0.0);
        assertThat(saved.getRatingCount()).isEqualTo(0);
    }

    @Test
    void shouldFindBookByTitle() {
        List<Book> found = bookRepository.findByTitle("The Hobbit");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getId()).isEqualTo(testBook.getId());
    }

    @Test
    void shouldFindBooksByPartialTitle() {
        Book anotherBook = createBook("The Hobbit: The Desolation of Smaug");
        bookRepository.save(anotherBook);

        List<Book> found = bookRepository.findByTitle("The Hobbit");

        assertThat(found).hasSize(2);
        assertThat(found).allMatch(book -> book.getTitle().contains("The Hobbit"));
    }

    @Test
    void shouldCheckExistsByTitle() {
        boolean exists = bookRepository.existsByTitle("The Hobbit");
        boolean notExists = bookRepository.existsByTitle("Nonexistent Book");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void shouldSaveBookWithAuthors() {
        Author author = new Author();
        author.setName("J.R.R. Tolkien");
        authorRepository.save(author);

        testBook.getBookAuthors().add(author);
        author.getBooks().add(testBook);
        bookRepository.save(testBook);

        Book found = bookRepository.findById(testBook.getId()).get();
        assertThat(found.getBookAuthors()).hasSize(1);
        assertThat(found.getBookAuthors().iterator().next().getName()).isEqualTo("J.R.R. Tolkien");
    }

//    @Test
//    void shouldSaveBookWithGenres() {
//        Genre genre = new Genre();
//        genre.setName("Fantasy");
//        genreRepository.save(genre);
//
//        testBook.getBookGenres().add(genre);
//        genre.getBooks().add(testBook);
//        bookRepository.save(testBook);
//
//        Book found = bookRepository.findById(testBook.getId()).get();
//        assertThat(found.getBookGenres()).hasSize(1);
//        assertThat(found.getBookGenres().iterator().next().getName()).isEqualTo("Fantasy");
//    }

    @Test
    void shouldUpdateBook() {
        testBook.setQuantity(10);
        testBook.setAvailable(false);
        bookRepository.save(testBook);

        Book updated = bookRepository.findById(testBook.getId()).get();
        assertThat(updated.getQuantity()).isEqualTo(10);
        assertThat(updated.isAvailable()).isFalse();
    }

    private Book createBook(String title) {
        Book book = new Book();
        book.setTitle(title);
        book.setReleasedAt(LocalDate.now());
        book.setDifficultLevel(ReadingLevel.EASY);
        book.setAvailable(true);
        book.setPageCount(200);
        return book;
    }
}