package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Author;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.Genre;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import com.vianavitor.simplelibrarygame.repository.AuthorRepository;
import com.vianavitor.simplelibrarygame.repository.BookRepository;
import com.vianavitor.simplelibrarygame.repository.GenreRepository;
import com.vianavitor.simplelibrarygame.service.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;

public class BookServiceTest extends BaseServiceTest {

    @Mock
    private BookRepository repository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private BookService service;

    private Book testBook;
    private Author testAuthor;
    private Genre testGenre;

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

        testAuthor = new Author();
        testAuthor.setName("J.R.R. Tolkien");

        testGenre = new Genre();
        testGenre.setName("Fantasy");
    }

    @Test
    public void testAddWithAuthorFounded() {
        testBook.getBookAuthors().add(testAuthor);
        testBook.getBookGenres().add(testGenre);

        when(repository.existsByTitle("The Hobbit"))
                .thenReturn(false);

        when(authorRepository.findByName("J.R.R. Tolkien"))
                .thenReturn(Optional.of(testAuthor));

        when(genreRepository.findByName("Fantasy"))
                .thenReturn(Optional.of(testGenre));

//        when(authorRepository.save(any(Author.class)))
//                .thenReturn(testAuthor);

        when(repository.save(any(Book.class)))
                .thenReturn(testBook);

        service.add(testBook, false);

        assertThat(testBook.getBookAuthors()).contains(testAuthor);
        assertThat(testBook.getBookGenres()).contains(testGenre);

        verify(repository, times(1)).existsByTitle("The Hobbit");
        verify(authorRepository, times(1)).findByName("J.R.R. Tolkien");
//        verify(authorRepository, times(1)).save(testAuthor);
        verify(genreRepository, times(1)).findByName("Fantasy");
        verify(repository, times(1)).save(testBook);
    }

    @Test
    public void testAddWithAuthorNotFounded() {
        testBook.getBookAuthors().add(testAuthor);
        testBook.getBookGenres().add(testGenre);

        when(repository.existsByTitle("The Hobbit"))
                .thenReturn(false);

        when(authorRepository.findByName("J.R.R. Tolkien"))
                .thenReturn(Optional.empty());

        when(genreRepository.findByName("Fantasy"))
                .thenReturn(Optional.of(testGenre));

        when(authorRepository.save(any(Author.class)))
                .thenReturn(testAuthor);

        when(repository.save(any(Book.class)))
                .thenReturn(testBook);

        service.add(testBook, false);

        assertThat(testBook.getBookAuthors()).contains(testAuthor);
        assertThat(testBook.getBookGenres()).contains(testGenre);

        verify(repository, times(1)).existsByTitle("The Hobbit");
        verify(authorRepository, times(1)).findByName("J.R.R. Tolkien");
        verify(authorRepository, times(1)).save(testAuthor);
        verify(genreRepository, times(1)).findByName("Fantasy");
        verify(repository, times(1)).save(testBook);
    }

    @Test
    public void testRate() {
        testBook.setRatingCount(10);
        testBook.setRatingValue(3.0);

        int rate = 4;

        when(repository.findById(1L))
                .thenReturn(Optional.of(testBook));

        when(repository.save(any(Book.class)))
                .thenReturn(testBook);

        service.rate(1L, rate);

        assertThat(testBook.getRatingCount()).isGreaterThan(10);
        assertThat(testBook.getRatingValue()).isEqualTo(3.090909090909091);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(testBook);
    }

    @Test
    public void testSetAvailableToFalse() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(testBook));

        when(repository.save(any(Book.class)))
                .thenReturn(testBook);

        service.setAvailable(1L, false);

        assertThat(testBook.isAvailable()).isFalse();

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(testBook);
    }

    @Test
    public void testSetAvailableToTrue() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(testBook));

        when(repository.save(any(Book.class)))
                .thenReturn(testBook);

        service.setAvailable(1L, true);

        assertThat(testBook.isAvailable()).isTrue();

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(testBook);
    }

    @Test
    public void testModifyButNotFoundByTitle() {
        testBook.setId(1L);

        String title = "The Hobbit, or There and Back Again";

        Book data = new Book();
        data.setTitle(title);

        Map<Long, Book> cache = new HashMap<>();

        when(repository.findById(1L))
                .thenReturn(Optional.of(testBook));

        when(repository.findByTitle(title))
                .thenReturn(List.of());

        when(repository.save(any(Book.class)))
                .thenReturn(testBook);

        Book result = service.modify(1L, data, false, cache);

        assertThat(result.getTitle()).isEqualTo(title);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).findByTitle(title);
        verify(repository, times(1)).save(testBook);
    }

    @Test
    public void testModifyButFoundByTitle() {
        testBook.setId(1L);

        String title = "The Hobbit, or There and Back Again";

        Book data = new Book();
        data.setTitle(title);

        Map<Long, Book> cache = new HashMap<>();

        when(repository.findById(1L))
                .thenReturn(Optional.of(testBook));

        when(repository.save(any(Book.class)))
                .thenReturn(testBook);

        Book result = service.modify(1L, data, true, cache);

        assertThat(cache).containsKey(1L);
        assertThat(result.getTitle()).isEqualTo(title);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(testBook);
    }

    @Test
    public void testModifyTitleWithCache() {
        testBook.setId(1L);

        String title = "The Hobbit, or There and Back Again";

        Book data = new Book();
        data.setTitle(title);

        Map<Long, Book> cache = new HashMap<>();
        cache.put(1L, testBook);

        when(repository.save(any(Book.class)))
                .thenReturn(testBook);

        Book result = service.modify(1L, data, true, cache);

        assertThat(cache).isNotEmpty();
        assertThat(cache).containsKey(1L);
        assertThat(result.getTitle()).isEqualTo(title);

        verify(repository, times(1)).save(testBook);
    }

    @Test
    public void testModifyAuthorAndGenreButNotFoundByTitle() {
        testBook.setId(1L);

        String title = "The Hobbit, or There and Back Again";
        String authorName = "John Ronald Reuel Tolkien";
        String genreName = "Adventure";

        testAuthor.setName(authorName);
        testGenre.setName(genreName);

        Book data = new Book();
        data.setTitle(title);
        data.getBookGenres().add(testGenre);
        data.getBookAuthors().add(testAuthor);

        Map<Long, Book> cache = new HashMap<>();

        when(repository.findById(1L))
                .thenReturn(Optional.of(testBook));

        when(repository.findByTitle(title))
                .thenReturn(List.of());

        when(authorRepository.findByName("John Ronald Reuel Tolkien"))
                .thenReturn(Optional.of(testAuthor));

        when(genreRepository.findByName("Adventure"))
                .thenReturn(Optional.of(testGenre));

        when(repository.save(any(Book.class)))
                .thenReturn(testBook);

        Book result = service.modify(1L, data, false, cache);

        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getBookGenres()).contains(testGenre);
        assertThat(result.getBookAuthors()).contains(testAuthor);

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).findByTitle(title);
        verify(authorRepository, times(1)).findByName("John Ronald Reuel Tolkien");
        verify(genreRepository, times(1)).findByName("Adventure");
        verify(repository, times(1)).save(testBook);
    }
}
