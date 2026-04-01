package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.repository.BookReadHistoryRepository;
import com.vianavitor.simplelibrarygame.repository.BookRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import com.vianavitor.simplelibrarygame.repository.StudentStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BookReadHistoryService {
    @Autowired
    private BookReadHistoryRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentStatsRepository statsRepository;

    public void register(BookReadHistory data) {
        Book book = bookRepository.findById(data.getBook().getId())
                .orElseThrow(() -> new RuntimeException("not found book"));

        Student student = studentRepository.findById(data.getUser().getId())
                .orElseThrow(() -> new RuntimeException("not found student"));

        Optional<BookReadHistory> result = repository.findByStudent(student)
                .stream()
//                check if the student has already read this book before...
                .filter((s) ->
                        Objects.equals(s.getBook().getId(), book.getId())
                ).findFirst();

        if (result.isEmpty()) {
            data.setBook(book);
            data.setUser(student);
        } else {
            int page = data.getLastPageRead();

            data = result.get();
            data.setLastPageRead(page);
        }

        data.setLastUpdate(LocalDate.now());

        BookReadHistory current = repository.save(data);
        student.getStats().setCurrentBook(current);

        statsRepository.save(student.getStats());
    }

    public List<BookReadHistory> getByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("not found student"));

        return repository.findByStudent(student);
    }

    public List<BookReadHistory> getByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("not found book"));

        return repository.findByBook(book);
    }

    public BookReadHistory getByStudentTheLastOne(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("not found student"));

        AtomicReference<BookReadHistory> lastOne = new AtomicReference<>();
        repository.findByStudent(student)
                .forEach(lastOne::set);

        return lastOne.get();
    }
}
