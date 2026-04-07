package com.vianavitor.simplelibrarygame.controller;

import com.vianavitor.simplelibrarygame.dto.ApiResponse;
import com.vianavitor.simplelibrarygame.model.BookReadHistory;
import com.vianavitor.simplelibrarygame.service.BookReadHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<BookReadHistory>>> getByStudent(@PathVariable Long studentId, HttpServletRequest request) {
        List<BookReadHistory> list = historyService.getByStudent(studentId);
        ApiResponse<List<BookReadHistory>> response = ApiResponse.success(list, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<BookReadHistory>>> getByBook(@PathVariable Long bookId, HttpServletRequest request) {
        List<BookReadHistory> list = historyService.getByBook(bookId);
        ApiResponse<List<BookReadHistory>> response = ApiResponse.success(list, request.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}/last")
    public ResponseEntity<ApiResponse<BookReadHistory>> getLastByStudent(@PathVariable Long studentId, HttpServletRequest request) {
        BookReadHistory last = historyService.getByStudentTheLastOne(studentId);
        ApiResponse<BookReadHistory> response = ApiResponse.success(last, request.getRequestURI());
        return ResponseEntity.ok(response);
    }
}