package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.request.SubmitSummaryRequest;
import com.vianavitor.simplelibrarygame.model.BookSummary;
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

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<BookSummary>>> getByStudent(@PathVariable Long studentId, HttpServletRequest request) {
        List<BookSummary> summaries = summaryService.getByStudent(studentId);
        ApiResponse<List<BookSummary>> response = ApiResponse.success(summaries, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<BookSummary>>> getByBook(@PathVariable Long bookId, HttpServletRequest request) {
        List<BookSummary> summaries = summaryService.getByBook(bookId);
        ApiResponse<List<BookSummary>> response = ApiResponse.success(summaries, request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}