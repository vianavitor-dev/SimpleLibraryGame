package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.SubmitSummaryRequest;
import com.vianavitor.simplelibrarygame.dto.response.BookSummaryResponse;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookSummary;
import com.vianavitor.simplelibrarygame.model.Student;
import com.vianavitor.simplelibrarygame.service.BookSummaryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/summaries")
public class BookSummaryController {

    @Autowired
    private BookSummaryService summaryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> submitSummary(@Valid @RequestBody SubmitSummaryRequest request,
                                                           HttpServletRequest req) {
        summaryService.submit(request.text(), request.bookId(), request.studentId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(null, "Summary submitted", req.getRequestURI()));
    }

    private BookSummaryResponse bookSummaryToDto(BookSummary summary) {
        Student student = summary.getStudent();
        Book book = summary.getBook();

        return new BookSummaryResponse(
                summary.getId(), summary.getText(),
                new BookSummaryResponse.StudentInfo(
                        student.getId(), student.getName()
                ),
                new BookSummaryResponse.BookInfo(
                        book.getId(), book.getTitle()
                ),
                summary.getWrittenAt()
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<BookSummaryResponse>>> getByStudent(@PathVariable Long studentId, HttpServletRequest request) {
        List<BookSummaryResponse> summaries = summaryService.getByStudent(studentId)
                .stream()
                .map(this::bookSummaryToDto)
                .toList();

        ApiResponse<List<BookSummaryResponse>> response = ApiResponse.success(summaries, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<BookSummaryResponse>>> getByBook(@PathVariable Long bookId, HttpServletRequest request) {
        List<BookSummaryResponse> summaries = summaryService.getByBook(bookId)
                .stream()
                .map(this::bookSummaryToDto)
                .toList();

        ApiResponse<List<BookSummaryResponse>> response = ApiResponse.success(summaries, request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}