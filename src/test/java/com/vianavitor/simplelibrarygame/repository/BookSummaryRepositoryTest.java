package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookSummary;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookSummaryRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private BookSummaryRepository summaryRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookRepository bookRepository;

    private Student testStudent;
    private Book testBook;
    private BookSummary testSummary;

    @BeforeEach
    void setUp() {
        testStudent = new Student("student1", "pass");
        testStudent.setName("Jane Doe");
        studentRepository.save(testStudent);

        testBook = createBook("Great Expectations");
        bookRepository.save(testBook);

        testSummary = new BookSummary();
        testSummary.setStudent(testStudent);
        testSummary.setBook(testBook);
        testSummary.setText("This is a great book about...");
        testSummary.setWrittenAt(LocalDate.now());
        summaryRepository.save(testSummary);
    }

    @Test
    void shouldSaveSummary() {
        BookSummary newSummary = new BookSummary();
        newSummary.setStudent(testStudent);
        newSummary.setBook(testBook);
        newSummary.setText("Another summary");
        newSummary.setWrittenAt(LocalDate.now());

        BookSummary saved = summaryRepository.save(newSummary);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getText()).isEqualTo("Another summary");
        assertThat(saved.getWrittenAt()).isNotNull();
    }

    @Test
    void shouldFindSummaryByStudent() {
        List<BookSummary> summaries = summaryRepository.findByStudent(testStudent);

        assertThat(summaries).hasSize(1);
        assertThat(summaries.get(0).getText()).isEqualTo("This is a great book about...");
    }

    @Test
    void shouldFindSummaryByBook() {
        List<BookSummary> summaries = summaryRepository.findByBook(testBook);

        assertThat(summaries).hasSize(1);
        assertThat(summaries.get(0).getStudent().getUsername()).isEqualTo("student1");
    }

    @Test
    void shouldReturnMultipleSummariesForSameBook() {
        Student anotherStudent = new Student("student2", "pass");
        studentRepository.save(anotherStudent);

        BookSummary secondSummary = new BookSummary();
        secondSummary.setStudent(anotherStudent);
        secondSummary.setBook(testBook);
        secondSummary.setText("Another perspective");
        secondSummary.setWrittenAt(LocalDate.now());

        summaryRepository.save(secondSummary);

        List<BookSummary> summaries = summaryRepository.findByBook(testBook);
        assertThat(summaries).hasSize(2);
    }

    @Test
    void shouldUpdateSummaryText() {
        testSummary.setText("Updated summary text");
        summaryRepository.save(testSummary);

        BookSummary updated = summaryRepository.findById(testSummary.getId()).get();
        assertThat(updated.getText()).isEqualTo("Updated summary text");
    }

    @Test
    void shouldReturnEmptyListWhenNoSummaries() {
        Book newBook = createBook("New Book");
        bookRepository.save(newBook);

        List<BookSummary> summaries = summaryRepository.findByBook(newBook);
        assertThat(summaries).isEmpty();
    }

    private Book createBook(String title) {
        Book book = new Book();
        book.setTitle(title);
        book.setReleasedAt(LocalDate.now());
        book.setDifficultLevel(ReadingLevel.MEDIUM);
        book.setAvailable(true);
        book.setPageCount(300);
        return book;
    }
}