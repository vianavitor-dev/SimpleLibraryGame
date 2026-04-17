package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.response.UsersBookReadHistoryResponse;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.service.BookReadHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/read-history")
public class BookReadHistoryController {

    @Autowired
    private BookReadHistoryService historyService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody BookReadHistory history, HttpServletRequest request) {
        historyService.register(history);
        ApiResponse<Void> response = ApiResponse.success(null, "Reading progress saved", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<BookReadHistory>> getById(@PathVariable Long id, HttpServletRequest request) {
        BookReadHistory result = historyService.getById(id);
        ApiResponse<BookReadHistory> response = ApiResponse.success(result, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    private UsersBookReadHistoryResponse bookReadHistoryToDto(BookReadHistory history) {
        Book book = history.getBook();

        return new UsersBookReadHistoryResponse(
                history.getId(),
                new UsersBookReadHistoryResponse.BookInfo(
                        book.getId(),
                        book.getTitle()
                ),
                history.getLastUpdate(), history.getLastPageRead()
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<UsersBookReadHistoryResponse>>> getByStudent(@PathVariable Long studentId, HttpServletRequest request) {
        List<UsersBookReadHistoryResponse> list = historyService.getByStudent(studentId)
                .stream()
                .map(this::bookReadHistoryToDto)
                .toList();

        ApiResponse<List<UsersBookReadHistoryResponse>> response = ApiResponse.success(list, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<BookReadHistory>>> getByBook(@PathVariable Long bookId, HttpServletRequest request) {
        List<BookReadHistory> list = historyService.getByBook(bookId);
        ApiResponse<List<BookReadHistory>> response = ApiResponse.success(list, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}/last")
    public ResponseEntity<ApiResponse<UsersBookReadHistoryResponse>> getLastByStudent(@PathVariable Long studentId, HttpServletRequest request) {
        UsersBookReadHistoryResponse last = this.bookReadHistoryToDto(
                historyService.getByStudentTheLastOne(studentId)
        );

        ApiResponse<UsersBookReadHistoryResponse> response = ApiResponse.success(last, request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}