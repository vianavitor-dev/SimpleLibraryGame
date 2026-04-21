package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Author;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import com.vianavitor.simplelibrarygame.repository.AuthorRepository;
import com.vianavitor.simplelibrarygame.service.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AuthorServiceTest extends BaseServiceTest {
    @Mock
    private AuthorRepository repository;

    @InjectMocks
    private AuthorService service;

    @Captor
    private ArgumentCaptor<Author> authorCaptor;

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = new Author();
        testAuthor.setName("J.K. Rowling");
    }

    @Test
    public void testAdd() {
        when(repository.findByName("J.K. Rowling"))
                .thenReturn(Optional.empty());

        when(repository.save(any(Author.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

        Author result = service.add("J.K. Rowling");

        verify(repository, times(1)).save(authorCaptor.capture());

        Author saved = authorCaptor.getValue();

        assertThat(result.getName()).isEqualTo("J.K. Rowling");
        assertThat(saved.getName()).isEqualTo("J.K. Rowling");
    }

    @Test
    public void testAddDuplicateAuthorExpectingException() {
        when(repository.findByName("J.K. Rowling"))
                .thenReturn(Optional.of(testAuthor));

        assertThrows(Exception.class, () -> {
            service.add("J.K. Rowling");
        });

        assertThat(testAuthor.getName()).isEqualTo("J.K. Rowling");
    }

    @Test
    public void testGetAuthorsBooks() {
        Book book = new Book();
        book.setTitle("Harry Potter");
        book.setReleasedAt(LocalDate.now());
        book.setDifficultLevel(ReadingLevel.EASY);
        book.setAvailable(true);
        book.setPageCount(300);

        book.getBookAuthors().add(testAuthor);
        testAuthor.getBooks().add(book);

        when(repository.findById(1L))
                .thenReturn(Optional.of(testAuthor));

        List<Book> results = service.getAuthorBooks(1L);

        assertThat(results).isNotEmpty();
        assertThat(results.size()).isEqualTo(1);
        assertThat(results).contains(book);

        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void testGetAuthorsBooksException() {
        Book book = new Book();
        book.setTitle("Harry Potter");
        book.setReleasedAt(LocalDate.now());
        book.setDifficultLevel(ReadingLevel.EASY);
        book.setAvailable(true);
        book.setPageCount(300);

        book.getBookAuthors().add(testAuthor);
        testAuthor.getBooks().add(book);

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            service.getAuthorBooks(1L);
        });

        verify(repository, times(1)).findById(1L);
    }
}
