package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.model.StudentStats;
import com.vianavitor.simplelibrarygame.model.utils.fields.ReadingLevel;
import com.vianavitor.simplelibrarygame.repository.BookReadHistoryRepository;
import com.vianavitor.simplelibrarygame.repository.BookRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.repository.StudentStatsRepository;
import com.vianavitor.simplelibrarygame.service.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookReadHistoryServiceTest extends BaseServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentStatsRepository statsRepository;

    @Mock
    private BookReadHistoryRepository repository;

    @InjectMocks
    private BookReadHistoryService service;

    private Student testStudent;
    private Book testBook;
    private BookReadHistory testHistory;
    
    @BeforeEach
    void setUp() {
        testStudent = new Student("reader1", "pass");
        testStudent.setName("John Reader");
        testStudent.setStats(new StudentStats());

        testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setReleasedAt(LocalDate.now());
        testBook.setDifficultLevel(ReadingLevel.EASY);
        testBook.setAvailable(true);
        testBook.setPageCount(200);

        testHistory = new BookReadHistory();
        testHistory.setUser(testStudent);
        testHistory.setBook(testBook);
        testHistory.setLastPageRead(50);
        testHistory.setLastUpdate(LocalDate.now());
    }

    @Test
    public void testRegister() {
        Book book = new Book();
        book.setId(1L);

        Student student = new Student();
        student.setId(1L);

        BookReadHistory history = new BookReadHistory();
        history.setUser(student);
        history.setBook(book);
        history.setLastPageRead(80);

//        testBook.setId(1L);
//        testStudent.setId(1L);

        List<BookReadHistory> list = new ArrayList<>();
        list.add(testHistory);

        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(testBook));

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(testStudent));

        when(repository.findByStudent(any(Student.class)))
                .thenReturn(list);

        when(repository.save(any(BookReadHistory.class)))
                .thenReturn(testHistory);

        when(statsRepository.save(any(StudentStats.class)))
                .thenReturn(null);

        service.register(history);

        assertThat(testHistory.getLastUpdate()).isToday();
        assertThat(testHistory.getLastPageRead()).isEqualTo(80);
        assertThat(testHistory.getBook().getTitle()).isEqualTo("Test Book");
        assertThat(testHistory.getUser().getUsername()).isEqualTo("reader1");

        verify(bookRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).findById(1L);
        verify(repository, times(1)).findByStudent(testStudent);
        verify(repository, times(1)).save(testHistory);
        verify(statsRepository, times(1)).save(testStudent.getStats());
    }

    @Test
    public void testGetByStudentTheLastOne() {
        Student student = new Student("reader2", "pass");
        student.setName("John Reader 2");
        student.setStats(new StudentStats());

        Book book = new Book();
        book.setTitle("Test Book 2");
        book.setReleasedAt(LocalDate.now());
        book.setDifficultLevel(ReadingLevel.EASY);
        book.setAvailable(true);
        book.setPageCount(200);

        BookReadHistory history = new BookReadHistory();
        history.setUser(student);
        history.setBook(book);
        history.setLastPageRead(20);
        history.setLastUpdate(LocalDate.now());

        List<BookReadHistory> list = new ArrayList<>();
        list.add(history);
        list.add(testHistory);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(testStudent));

        when(repository.findByStudent(any(Student.class)))
                .thenReturn(list);

        BookReadHistory result = service.getByStudentTheLastOne(1L);

        assertThat(result).isNotNull();
        assertThat(result.getBook().getTitle()).isEqualTo("Test Book");

        verify(studentRepository, times(1)).findById(1L);
        verify(repository, times(1)).findByStudent(testStudent);
    }
}
