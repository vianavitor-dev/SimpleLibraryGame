package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.dto.response.BookReadHistoryResponse;
import com.vianavitor.simplelibrarygame.dto.response.UsersBookReadHistoryResponse;
import com.vianavitor.simplelibrarygame.model.Book;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.model.Student;
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

    private BookReadHistoryResponse BookReadHistoryToDto(BookReadHistory history) {
        Book book = history.getBook();
        Student student = history.getUser();

        return new BookReadHistoryResponse(
                history.getId(),
                new BookReadHistoryResponse.StudentInfo(
                        student.getId(),
                        student.getName()
                ),
                new UsersBookReadHistoryResponse.BookInfo(
                        book.getId(),
                        book.getTitle()
                ),
                history.getLastUpdate(), history.getLastPageRead()
        );
    }

    private UsersBookReadHistoryResponse bookReadHistoryToUsersDto(BookReadHistory history) {
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

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody BookReadHistory history, HttpServletRequest request) {
        historyService.register(history);
        ApiResponse<Void> response = ApiResponse.success(null, "Reading progress saved", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<BookReadHistoryResponse>> getById(@PathVariable Long id, HttpServletRequest request) {
        BookReadHistory result = historyService.getById(id);
        ApiResponse<BookReadHistoryResponse> response = ApiResponse.success(this.BookReadHistoryToDto(result), request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<UsersBookReadHistoryResponse>>> getByStudent(@PathVariable Long studentId, HttpServletRequest request) {
        List<UsersBookReadHistoryResponse> list = historyService.getByStudent(studentId)
                .stream()
                .map(this::bookReadHistoryToUsersDto)
                .toList();

        ApiResponse<List<UsersBookReadHistoryResponse>> response = ApiResponse.success(list, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<BookReadHistoryResponse>>> getByBook(@PathVariable Long bookId, HttpServletRequest request) {
        List<BookReadHistoryResponse> list = historyService.getByBook(bookId)
                .stream()
                .map(this::BookReadHistoryToDto)
                .toList();

        ApiResponse<List<BookReadHistoryResponse>> response = ApiResponse.success(list, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}/last")
    public ResponseEntity<ApiResponse<UsersBookReadHistoryResponse>> getLastByStudent(@PathVariable Long studentId, HttpServletRequest request) {
        UsersBookReadHistoryResponse last = this.bookReadHistoryToUsersDto(
                historyService.getByStudentTheLastOne(studentId)
        );

        ApiResponse<UsersBookReadHistoryResponse> response = ApiResponse.success(last, request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}