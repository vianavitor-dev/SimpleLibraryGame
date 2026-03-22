package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.*;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookReadHistoryRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private BookReadHistoryRepository historyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Student testStudent;
    private Book testBook;
    private BookReadHistory testHistory;

    @BeforeEach
    void setUp() {
        testStudent = new Student("reader1", "pass");
        testStudent.setName("John Reader");
        studentRepository.save(testStudent);

        testBook = createBook("Test Book");
        bookRepository.save(testBook);

        testHistory = new BookReadHistory();
        testHistory.setUser(testStudent);
        testHistory.setBook(testBook);
        testHistory.setLastPageRead(50);
        testHistory.setLastUpdate(LocalDate.now());
        historyRepository.save(testHistory);
    }

    @Test
    void shouldSaveHistory() {
        BookReadHistory newHistory = new BookReadHistory();
        newHistory.setUser(testStudent);
        newHistory.setBook(testBook);
        newHistory.setLastPageRead(100);

        BookReadHistory saved = historyRepository.save(newHistory);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getLastPageRead()).isEqualTo(100);
        assertThat(saved.getLastUpdate()).isNotNull();
    }

    @Test
    void shouldFindHistoryByStudent() {
        List<BookReadHistory> histories = historyRepository.findByStudent(testStudent);

        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getBook().getTitle()).isEqualTo("Test Book");
    }

    @Test
    void shouldFindHistoryByBook() {
        List<BookReadHistory> histories = historyRepository.findByBook(testBook);

        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getUser().getUsername()).isEqualTo("reader1");
    }

    @Test
    void shouldUpdateLastPageRead() {
        testHistory.setLastPageRead(150);
        historyRepository.save(testHistory);

        BookReadHistory updated = historyRepository.findById(testHistory.getId()).get();
        assertThat(updated.getLastPageRead()).isEqualTo(150);
    }

    @Test
    void shouldReturnEmptyListWhenNoHistoryForStudent() {
        Student newStudent = new Student("new_reader", "pass");
        studentRepository.save(newStudent);

        List<BookReadHistory> histories = historyRepository.findByStudent(newStudent);

        assertThat(histories).isEmpty();
    }

    @Test
    void shouldReturnMultipleHistoriesForSameStudent() {
        Book anotherBook = createBook("Another Book");
        bookRepository.save(anotherBook);

        BookReadHistory secondHistory = new BookReadHistory();
        secondHistory.setUser(testStudent);
        secondHistory.setBook(anotherBook);
        secondHistory.setLastPageRead(25);
        historyRepository.save(secondHistory);

        List<BookReadHistory> histories = historyRepository.findByStudent(testStudent);
        assertThat(histories).hasSize(2);
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