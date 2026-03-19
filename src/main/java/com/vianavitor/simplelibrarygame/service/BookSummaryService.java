package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookSummary;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.repository.BookRepository;
import com.vianavitor.simplelibrarygame.repository.BookSummaryRepository;
import com.vianavitor.simplelibrarygame.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookSummaryService {

    @Autowired
    private BookSummaryRepository repository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudentRepository studentRepository;

    public void submit(String text, Long bookId, Long studentId)  {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("not found book"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("student not found"));

        BookSummary summary = new BookSummary();
        summary.setBook(book);
        summary.setStudent(student);

        repository.save(summary);
    }

    public List<BookSummary> getByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("student not found"));

        return repository.findByStudent(student);
    }

    public List<BookSummary> getByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("book not found"));

        return repository.findByBook(book);
    }
}
